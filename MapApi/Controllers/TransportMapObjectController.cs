using MapApi.Context;
using MapApi.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace MapApi.Controllers
{
    [Route("api/TransportlMapObject")]
    [ApiController]
    public class TransportMapObjectController : Controller
    {
        private readonly ApplicationContext _context;

        public TransportMapObjectController(ApplicationContext context)
        {
            _context = context;
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<TransportMapObject>>> GetTransportMapObject()
        {
            if (_context.TransportMapObject == null)
            {
                return NotFound();
            }
            return await _context.TransportMapObject.ToListAsync();
        }

        [HttpPost]
        public async Task<IActionResult> AddTransportMapObject(int x, int y, string display_name)
        {
            var TransportMapObject = new TransportMapObject
            {
                X = x,
                Y = y,
                Display_name = display_name
            };
            await _context.TransportMapObject.AddAsync(TransportMapObject);
            await _context.SaveChangesAsync();

            return Ok();
        }
    }
}
