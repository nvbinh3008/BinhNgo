using System.ComponentModel.DataAnnotations;

namespace SchoolMvc.Models;

/// <summary>
/// Represents a student in the system. This maps to the Student table.
/// </summary>
public class Student
{
    /// <summary>
    /// Primary key generated automatically by SQL Server.
    /// </summary>
    public int Id { get; set; }

    /// <summary>
    /// Full name of the student.
    /// </summary>
    [Required]
    [StringLength(100)]
    public string Name { get; set; } = string.Empty;

    /// <summary>
    /// Date of birth. Stored as DATE in the database.
    /// </summary>
    [DataType(DataType.Date)]
    public DateOnly Dob { get; set; }

    /// <summary>
    /// Many-to-many relationship with courses through StudentCourse join table.
    /// </summary>
    public ICollection<StudentCourse> StudentCourses { get; set; } = new List<StudentCourse>();
}
