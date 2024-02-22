using MapApi.Context;
using MapApi.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace MapApi.Controllers
{
    [Route("api/comment")]
    [ApiController]
    public class CommentController : Controller
    {
        private readonly ApplicationContext _context;

        public CommentController(ApplicationContext context)
        {
            _context = context;
        }
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Comment>>> GetComments()
        {
            if (_context.Comment == null)
            {
                return NotFound();
            }
            return await _context.Comment.ToListAsync();
        }

        [HttpPost]
        public async Task<IActionResult> AddComment(string text)
        {
            var Comment = new Comment
            {
                Text = text,
            };
            await _context.Comment.AddAsync(Comment);
            await _context.SaveChangesAsync();

            return Ok();
        }
    }
}
