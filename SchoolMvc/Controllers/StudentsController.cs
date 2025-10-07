using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SchoolMvc.Data;
using SchoolMvc.Models;

namespace SchoolMvc.Controllers;

/// <summary>
/// Provides actions to display and create students.
/// </summary>
public class StudentsController : Controller
{
    private readonly SchoolContext _context;

    /// <summary>
    /// Constructor injecting the database context.
    /// </summary>
    public StudentsController(SchoolContext context)
    {
        _context = context;
    }

    /// <summary>
    /// Shows the student list with their enrolled courses.
    /// </summary>
    public async Task<IActionResult> Index()
    {
        var students = await _context.Students
            .Include(s => s.StudentCourses)
                .ThenInclude(sc => sc.Course)
            .AsNoTracking()
            .ToListAsync();
        return View(students);
    }

    /// <summary>
    /// Display the create student form.
    /// </summary>
    public IActionResult Create()
    {
        return View();
    }

    /// <summary>
    /// Save a new student to the database.
    /// </summary>
    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Create([Bind("Name,Dob")] Student student)
    {
        if (!ModelState.IsValid)
        {
            return View(student);
        }

        _context.Students.Add(student);
        await _context.SaveChangesAsync();
        return RedirectToAction(nameof(Index));
    }
}
