using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SchoolMvc.Data;
using SchoolMvc.Models;

namespace SchoolMvc.Controllers;

/// <summary>
/// Handles CRUD operations for subjects.
/// </summary>
public class SubjectsController : Controller
{
    private readonly SchoolContext _context;

    /// <summary>
    /// Inject DbContext through constructor.
    /// </summary>
    public SubjectsController(SchoolContext context)
    {
        _context = context;
    }

    /// <summary>
    /// List all subjects.
    /// </summary>
    public async Task<IActionResult> Index()
    {
        var subjects = await _context.Subjects.AsNoTracking().ToListAsync();
        return View(subjects);
    }

    /// <summary>
    /// Render create form.
    /// </summary>
    public IActionResult Create()
    {
        return View();
    }

    /// <summary>
    /// Persist a new subject to the database.
    /// </summary>
    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Create([Bind("Name,NumberOfSlot")] Subject subject)
    {
        if (!ModelState.IsValid)
        {
            return View(subject);
        }

        _context.Subjects.Add(subject);
        await _context.SaveChangesAsync();
        return RedirectToAction(nameof(Index));
    }
}
