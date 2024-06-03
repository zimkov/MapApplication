using MapApi.Context;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;
using VDS.RDF.Query.Algebra;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;

var builder = WebApplication.CreateBuilder(args);

// Configure Kestrel to listen on port 5000 for HTTP


// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();


builder.WebHost.UseKestrel(serverOptions =>
{
    serverOptions.ListenAnyIP(5000); // Listen for HTTP on port 5000
});

var connection = "Host=localhost;Port=5432;Username=leha;Password=12345;Database=map";
builder.Services.AddDbContext<ApplicationContext>(options => {
    options.UseNpgsql(connection);
    options.UseQueryTrackingBehavior(QueryTrackingBehavior.NoTracking);
    });

var app = builder.Build();
app.UseHttpsRedirection();
// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

// Removed app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
