using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace MapApi.Models
{
    public class SocialMapObject : MapObject
    {
        [Column("Rating")]
        [Required]
        public int Rating { get; set; }
    }
}
