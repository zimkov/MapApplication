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
        [Route("api/SocialMapObject/getbyid")]
        public string GetOntologyObjects()
        {
            //Create a graph to load into
            IGraph g = new Graph();

            //Load the OWL file
            g.LoadFromFile("Ontology.owl");

            string s = "Результат запроса: ";
            

            //First we need an instance of the SparqlQueryParser
            SparqlQueryParser parser = new SparqlQueryParser();

            //Then we can parse a SPARQL string into a query
            SparqlQuery q = parser
                .ParseFromString("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT ?subject ?object WHERE { ?subject rdfs:subClassOf ?object }");
            Object results = g.ExecuteQuery(q);
            if (results is SparqlResultSet)
            {
                //Print out the Results
                SparqlResultSet rset = (SparqlResultSet)results;
                foreach (SparqlResult result in rset)
                {
                    string decodedString = HttpUtility.UrlDecode(result.ToString());
                    //int i = decodedString.IndexOf('#');
                   // string iri = decodedString.Substring(i);
                    Console.WriteLine(decodedString);
                    Console.WriteLine("\n");
                }
            }

            return s;
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
    }
}
