using Microsoft.AspNetCore.Mvc;

namespace SchoolMvc.Controllers;

/// <summary>
/// Controller serving the landing pages of the application.
/// </summary>
public class HomeController : Controller
{
    /// <summary>
    /// Displays the home page with navigation links.
    /// </summary>
    public IActionResult Index()
    {
        return View();
    }

    /// <summary>
    /// Displays a simple error page.
    /// </summary>
    public IActionResult Error()
    {
        return View();
    }
}
