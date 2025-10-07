using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using SchoolMvc.Data;
using SchoolMvc.Models;

namespace SchoolMvc.Controllers;

/// <summary>
/// Manages course pages and enrollment forms.
/// </summary>
public class CoursesController : Controller
{
    private readonly SchoolContext _context;

    /// <summary>
    /// Constructor where dependency injection provides the DbContext.
    /// </summary>
    public CoursesController(SchoolContext context)
    {
        _context = context;
    }

    /// <summary>
    /// Show a list of courses with subjects and enrollment counts.
    /// </summary>
    public async Task<IActionResult> Index()
    {
        var courses = await _context.Courses
            .Include(c => c.Subject)
            .Include(c => c.StudentCourses)
            .AsNoTracking()
            .ToListAsync();
        return View(courses);
    }

    /// <summary>
    /// Render the create form.
    /// </summary>
    public IActionResult Create()
    {
        ViewData["SubjectId"] = new SelectList(_context.Subjects.AsNoTracking(), "Id", "Name");
        return View();
    }

    /// <summary>
    /// Persist the new course.
    /// </summary>
    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Create([Bind("Title,StartDate,EndDate,SubjectId")] Course course)
    {
        // Simple validation to ensure the course dates make sense.
        if (course.EndDate < course.StartDate)
        {
            ModelState.AddModelError(string.Empty, "End date must be on or after the start date.");
        }

        if (!ModelState.IsValid)
        {
            ViewData["SubjectId"] = new SelectList(_context.Subjects.AsNoTracking(), "Id", "Name", course.SubjectId);
            return View(course);
        }

        _context.Courses.Add(course);
        await _context.SaveChangesAsync();
        return RedirectToAction(nameof(Index));
    }
}
