using MapApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MapApi.Context
{
    public class ApplicationContext : DbContext
    {
        public DbSet<User> Users => Set<User>();
        public DbSet<MapObject> MapObject => Set<MapObject>();
        public DbSet<SocialMapObject> SocialMapObject => Set<SocialMapObject>();
        public DbSet<RoadMapObject> RoadMapObject => Set<RoadMapObject>();

        public ApplicationContext(DbContextOptions<ApplicationContext> options)
            : base(options)
        {
            Database.EnsureCreated();   // создаем базу данных при первом обращении
        }
    }
}
