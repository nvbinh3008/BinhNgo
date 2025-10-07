using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using SchoolMvc.Models;

namespace SchoolMvc.Data;

/// <summary>
/// Entity Framework Core DbContext that maps the .NET models to the SQL Server tables.
/// </summary>
public class SchoolContext : DbContext
{
    // Reusable converter/comparer pair to map DateOnly to SQL Server's date type.
    private static readonly ValueConverter<DateOnly, DateTime> DateOnlyConverter = new(
        convertToProviderExpression: d => d.ToDateTime(TimeOnly.MinValue),
        convertFromProviderExpression: dt => DateOnly.FromDateTime(dt));

    private static readonly ValueComparer<DateOnly> DateOnlyComparer = new(
        equalsExpression: (d1, d2) => d1 == d2,
        hashCodeExpression: d => d.GetHashCode());

    /// <summary>
    /// Constructor used by dependency injection.
    /// </summary>
    public SchoolContext(DbContextOptions<SchoolContext> options)
        : base(options)
    {
    }

    /// <summary>
    /// DbSet representing the Subject table.
    /// </summary>
    public DbSet<Subject> Subjects => Set<Subject>();

    /// <summary>
    /// DbSet representing the Student table.
    /// </summary>
    public DbSet<Student> Students => Set<Student>();

    /// <summary>
    /// DbSet representing the Course table.
    /// </summary>
    public DbSet<Course> Courses => Set<Course>();

    /// <summary>
    /// DbSet representing the StudentCourse table.
    /// </summary>
    public DbSet<StudentCourse> StudentCourses => Set<StudentCourse>();

    /// <summary>
    /// Configure entity relationships and column mappings to match the SQL schema.
    /// </summary>
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        // Configure Subject entity.
        modelBuilder.Entity<Subject>(entity =>
        {
            entity.ToTable("Subject");
            entity.Property(s => s.Name).HasMaxLength(100).IsRequired();
        });

        // Configure Student entity.
        modelBuilder.Entity<Student>(entity =>
        {
            entity.ToTable("Student");
            entity.Property(s => s.Name).HasMaxLength(100).IsRequired();
            entity.Property(s => s.Dob)
                .HasColumnType("date")
                .HasConversion(DateOnlyConverter)
                .Metadata.SetValueComparer(DateOnlyComparer);
        });

        // Configure Course entity and relationship to Subject.
        modelBuilder.Entity<Course>(entity =>
        {
            entity.ToTable("Course");
            entity.Property(c => c.Title).HasMaxLength(100).IsRequired();
            entity.Property(c => c.StartDate)
                .HasColumnType("date")
                .HasConversion(DateOnlyConverter)
                .Metadata.SetValueComparer(DateOnlyComparer);
            entity.Property(c => c.EndDate)
                .HasColumnType("date")
                .HasConversion(DateOnlyConverter)
                .Metadata.SetValueComparer(DateOnlyComparer);

            entity.HasOne(c => c.Subject)
                  .WithMany(s => s.Courses)
                  .HasForeignKey(c => c.SubjectId);
        });

        // Configure StudentCourse join table with composite key.
        modelBuilder.Entity<StudentCourse>(entity =>
        {
            entity.ToTable("StudentCourse");
            entity.HasKey(sc => new { sc.StudentId, sc.CourseId });
            entity.Property(sc => sc.Mark).HasColumnType("float");

            entity.HasOne(sc => sc.Student)
                  .WithMany(s => s.StudentCourses)
                  .HasForeignKey(sc => sc.StudentId);

            entity.HasOne(sc => sc.Course)
                  .WithMany(c => c.StudentCourses)
                  .HasForeignKey(sc => sc.CourseId);
        });
    }
}
