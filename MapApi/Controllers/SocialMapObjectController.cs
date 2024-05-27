using MapApi.Context;
using MapApi.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Web;
using VDS.RDF;
using VDS.RDF.Ontology;
using VDS.RDF.Parsing;
using VDS.RDF.Query;
using static Lucene.Net.Documents.Field;

namespace MapApi.Controllers
{
    [Route("api/SocialMapObject")]
    [ApiController]
    public class SocialMapObjectController : Controller
    {
        private readonly ApplicationContext _context;

        public SocialMapObjectController(ApplicationContext context)
        {
            _context = context;
        }
        [HttpGet]
        [Route("/get/ontology")]
        public async Task<ActionResult<IEnumerable<SocialMapObject>>> GetOntologyObjects()
        {
            IGraph g = new Graph();
            g.LoadFromFile("Ontology.owl");

            string s = "Результат запроса: \n";
            List<SocialMapObject> mapObjects = new List<SocialMapObject>();
            
            SparqlQueryParser parser = new SparqlQueryParser();
            SparqlQuery q = parser
                .ParseFromString("PREFIX obj: <http://www.semanticweb.org/алексей/ontologies/2023/8/untitled-ontology-44#>" +
                "SELECT  ?object ?x ?y ?type ?propertyValue WHERE { ?object obj:X ?x . ?object obj:Y ?y . ?object obj:является ?type . OPTIONAL {     ?object obj:категория_К ?propertyValue .   } }");
            Object results = g.ExecuteQuery(q);
            if (results is SparqlResultSet)
            {
                //Print out the Results
                SparqlResultSet rset = (SparqlResultSet)results;
                foreach (SparqlResult result in rset)
                {
                    string decodedString = HttpUtility.UrlDecode(result.ToString());
                    string[] separatingStrings = { "?object = http://www.semanticweb.org/safon/ontologies/2023/11/untitled-ontology-24#", "?object = http://www.semanticweb.org/алексей/ontologies/2023/8/untitled-ontology-44#", " , ?x = ", "^^http://www.w3.org/2001/XMLSchema#decimal , ?y = ", "^^http://www.w3.org/2001/XMLSchema#decimal", " , ?type = http://www.semanticweb.org/алексей/ontologies/2023/8/untitled-ontology-44#", " , ?propertyValue = ", "^^http://www.w3.org/2001/XMLSchema#boolean" };

                    string text = decodedString;
                    var availabString = "";
                    string[] words = text.Split(separatingStrings, System.StringSplitOptions.RemoveEmptyEntries);
                    if (words.Length <= 4)
                    {
                        availabString = "нет_информации_о_доступности";
                    }
                    else
                    {
                        availabString = "Доступен";
                    }
                    var socialMapObject = new SocialMapObject
                    {
                        Display_name = words[0].ToString(),
                        X = double.Parse(words[2], System.Globalization.CultureInfo.InvariantCulture),
                        Y = double.Parse(words[3], System.Globalization.CultureInfo.InvariantCulture),
                        Type = words[1],
                        Availability = availabString
                    };
                    var existingEntity = _context.SocialMapObject.AsNoTracking().FirstOrDefault<SocialMapObject>(e => e.Display_name == socialMapObject.Display_name);
                    _context.SaveChanges();
                    if (existingEntity != null)
                    {
                        
                        socialMapObject.Id = existingEntity.Id;
                        
                        //_context.Entry(socialMapObject).State = EntityState.Modified;
                        _context.SocialMapObject.Update(socialMapObject);
                        _context.SaveChanges();
                        _context.Entry(socialMapObject).State = EntityState.Detached;


                    }
                    else
                    {
                        _context.SocialMapObject.Add(socialMapObject);
                        _context.SaveChanges();
                    }


                    await _context.SaveChangesAsync();

                    mapObjects.Add(socialMapObject);
                    
                    //Console.WriteLine(string.Join(" ", words));
                    //s += string.Join(" ", words) + "\n";
                    //Console.WriteLine("\n");
                }
                
                
            }
            
            return await _context.SocialMapObject.ToListAsync();
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<SocialMapObject>>> GetSocialMapObject()
        {
            if (_context.SocialMapObject == null)
            {
                return NotFound();
            }
            return await _context.SocialMapObject.ToListAsync();
        }

        [HttpPost]
        public async Task<IActionResult> AddSocialMapObject(int x, int y, string display_name, int rating)
        {
            var socialMapObject = new SocialMapObject
            {
                X = x,
                Y = y,
                Display_name = display_name,
                Rating = rating
            };
            await _context.SocialMapObject.AddAsync(socialMapObject);
            await _context.SaveChangesAsync();

            return Ok();
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<SocialMapObject>> GetSocialMapObjectById(int id)
        {
            var socialMapObject = await _context.SocialMapObject.FindAsync(id);
            if (socialMapObject == null)
            {
                return NotFound();
            }
            return socialMapObject;
        }
        

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            var socialMapObject = await _context.SocialMapObject.FindAsync(id);

            if (socialMapObject == null)
            {
                return NotFound();
            }

            _context.SocialMapObject.Remove(socialMapObject);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        [HttpPut("{name}")]
        public async Task<ActionResult> Put(string name, SocialMapObject socialMapObject)
        {
            if (name != socialMapObject.Display_name)
            {
                return BadRequest();
            }

            //_context.Entry(socialMapObject).State = EntityState.Modified;
            _context.SocialMapObject.Update(socialMapObject);
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!SocialMapObjectExists2(name))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        private bool SocialMapObjectExists(int id)
        {
            return (_context.SocialMapObject?.Any(e => e.Id == id)).GetValueOrDefault();
        }

        private bool SocialMapObjectExists2(string name)
        {
            return (_context.SocialMapObject?.Any(e => e.Display_name.Equals(name))).GetValueOrDefault();
        }
    }
}
