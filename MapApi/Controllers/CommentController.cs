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
            return _context.Comment.Include(c => c.User).Include(c => c.MapObject).ToList();
        }


        [HttpGet("{mapObjectId}")]
        public async Task<ActionResult<IEnumerable<Comment>>> GetCommentByMapObject(int mapObjectId)
        {
            if (_context.Comment == null)
            {
                return Problem("Entity set 'MvcMovieContext.Movie'  is null.");
            }

            var mapObject = await _context.SocialMapObject.FindAsync(mapObjectId);

            

            var comments = from m in _context.Comment
                        select m;

            comments = comments.Include(c => c.User).Where(s => s.MapObject.Equals(mapObject));
            
            return await comments.ToListAsync();
        }


        [HttpPost]
        public async Task<IActionResult> AddComment(string text, string email, int mapObjectId)
        {
             if (_context.Users == null)
            {
                return Problem("Entity set 'MvcMovieContext.Movie'  is null.");
            }

            var users = from m in _context.Users
                        select m;

            if (!String.IsNullOrEmpty(email))
            {
                users = users.Where(s => s.Email!.Contains(email));
            }


            var Comment = new Comment
            {
                Text = text,
                Rate = 4,
                User = await users.FirstOrDefaultAsync(),
                MapObject = await _context.SocialMapObject.FindAsync(mapObjectId)
            };
            await _context.Comment.AddAsync(Comment);
            await _context.SaveChangesAsync();

            return Ok();
        }
    }
}
