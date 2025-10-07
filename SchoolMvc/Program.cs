using Microsoft.EntityFrameworkCore;
using SchoolMvc.Data;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// The DbContext is registered with the SQL Server provider so it can connect to the SchoolDB database.
builder.Services.AddDbContext<SchoolContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("SchoolDatabase")));

// Enable controllers with views for the MVC pattern.
builder.Services.AddControllersWithViews();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    // In production, use the exception handler route for friendly error pages.
    app.UseExceptionHandler("/Home/Error");
    // Enforce HTTPS for security when running in production.
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();

// Map the default route for MVC controllers.
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

// Start listening for incoming HTTP requests.
app.Run();
