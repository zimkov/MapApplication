using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace MapApi.Models
{
    public class User
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Column("Name")]
        [Required]
        public string Name { get; set; } = null!;

        [Column("Type")]
        [Required]
        public string Type { get; set; } = null!;

        [Column("Email")]
        [Required]
        public string Email { get; set; } = null!;

        [Column("ListRoutes")]
        [Required]
        public List<Route> ListRoutes { get; set; } = null!;

    }
}
