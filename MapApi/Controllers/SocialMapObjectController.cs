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
        public List<SocialMapObject> GetOntologyObjects()
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
                    mapObjects.Add(socialMapObject);
                    Console.WriteLine(string.Join(" ", words));
                    s += string.Join(" ", words) + "\n";
                    Console.WriteLine("\n");
                }
            }

            return mapObjects;
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

        [HttpPut("{id}")]
        public async Task<ActionResult> Put(int id, SocialMapObject socialMapObject)
        {
            if (id != socialMapObject.Id)
            {
                return BadRequest();
            }

            _context.Entry(socialMapObject).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!SocialMapObjectExists(id))
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
    }
}
