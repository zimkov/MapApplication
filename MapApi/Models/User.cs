using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.Runtime.Serialization;
using System.Text.Json.Serialization;

namespace MapApi.Models
{
    public class User
    {
        public User()
        {
        }
        

        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Column("Name")]
        [Required]
        public string Name { get; set; } = null!;

        
        [Column("Type")]
        [Required]
        [JsonConverter(typeof(JsonStringEnumConverter))]
        public UserStatus? Type { get; set; }

        [Column("Email")]
        [Required]
        public string Email { get; set; } = null!;

        [Column("Password")]
        [Required]
        public string Password { get; set; } = null!;

        [Column("ListRoutes")]
        [Required]
        public List<Route> ListRoutes { get; set; } = null!;

    }

    public enum UserStatus
    {
        Колясочник,
        Опорнодвигательный,
        Слепота,
        Глухота,
        Умственно
    }
}
