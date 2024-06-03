using MapApi.Context;
using MapApi.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace MapApi.Controllers
{
    [Route("api/RoadlMapObject")]
    [ApiController]
    public class RoadMapObjectController : Controller
    {
        private readonly ApplicationContext _context;

        public RoadMapObjectController(ApplicationContext context)
        {
            _context = context;
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<RoadMapObject>>> GetRoadMapObject()
        {
            if (_context.RoadMapObject == null)
            {
                return NotFound();
            }
            return await _context.RoadMapObject.ToListAsync();
        }

        [HttpPost]
        public async Task<IActionResult> AddRoadMapObject(double x, double y, string display_name)
        {
            var roadMapObject = new RoadMapObject
            {
                X = x,
                Y = y,
                Display_name = display_name
            };
            await _context.RoadMapObject.AddAsync(roadMapObject);
            await _context.SaveChangesAsync();

            return Ok();
        }
    }
}
