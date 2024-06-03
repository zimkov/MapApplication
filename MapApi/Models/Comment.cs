using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace MapApi.Models
{
    public class Comment
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Column("Text")]
        public string? Text { get; set; } = null!;

        [Column("Rate")]
        public int Rate { get; set; }

        public int UserId { get; set; }

        [Column("User")]
        public User? User { get; set; } = null!;

        public int MapObjectId { get; set; }

        [Column("MapObject")]
        public MapObject? MapObject { get; set; } = null!;
    }
}
