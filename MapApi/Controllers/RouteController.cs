using MapApi.Context;
using MapApi.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace MapApi.Controllers
{
    [Route("api/routes")]
    [ApiController]
    public class RouteController : Controller
    {

        private readonly ApplicationContext _context;

        public RouteController(ApplicationContext context)
        {
            _context = context;
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Models.Route>>> GetRoutes()
        {
            if (_context.Route == null)
            {
                return NotFound();
            }
            return await _context.Route.ToListAsync();
        }

        [HttpPost]
        public async Task<IActionResult> AddRoute(string date)
        {
            var route = new Models.Route
            {
                Date = date
            };
            await _context.Route.AddAsync(route);
            await _context.SaveChangesAsync();

            return Ok();
        }
    }
}
