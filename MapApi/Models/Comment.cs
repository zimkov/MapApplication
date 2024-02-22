using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace MapApi.Models
{
    public class Comment
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Column("Text")]
        [Required]
        public string Text { get; set; } = null!;

        [Column("Rate")]
        [Required]
        public int Rate { get; set; }

        [Column("User")]
        [Required]
        public User User { get; set; } = null!;

        [Column("MapObject")]
        [Required]
        public MapObject MapObject { get; set; } = null!;
    }
}
