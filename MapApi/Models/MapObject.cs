using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.Runtime.Serialization;
using System.Text.Json.Serialization;

namespace MapApi.Models
{
    public abstract class MapObject
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Column("X")]
        [Required]
        public double X { get; set; }

        [Column("Y")]
        [Required]
        public double Y { get; set; }

        [Column("Display_name")]
        [Required]
        public string? Display_name { get; set; }

        [Column("Adress")]
        [Required]
        public string Adress { get; set; } = "нет адреса";

        [Column("Images")]
        [Required]
        public string Images { get; set; } = "нет картинки";

        [Column("Type")]
        [Required]
        public string Type { get; set; } = "тип не указан";

        [Column("Availability")]
        [Required]
        [JsonConverter(typeof(JsonStringEnumConverter))]
        public AvailabilityEnum? Availability{ get; set; }


        
    }

    public enum AvailabilityEnum
    {
        Нет_информации_о_доступности,
        Доступен,
        Доступен_с_ограничениями,
        Недоступен,
        
    }
}
