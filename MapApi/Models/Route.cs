using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace MapApi.Models
{
    public class Route
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Column("ListObjects")]
        [Required]
        public List<MapObject> ListObjects { get; set; } = null!;

        [Column("Date")]
        [Required]
        public string Date { get; set; } = null!;

    }
}
