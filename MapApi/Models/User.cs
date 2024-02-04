using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace MapApi.Models
{
    public class User
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int id { get; set; }

        [Column("name")]
        [Required]
        public string name { get; set; } = null!;

        [Column("type")]
        [Required]
        public string type { get; set; } = null!;

    }
}
