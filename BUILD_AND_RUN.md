# Build and Run Instructions - CustomerNinja Swing Edition

## ⚠️ Java Not Installed

The current system does not have Java installed. To compile and run the CustomerNinja POS application, you'll need to install a Java Development Kit (JDK).

## Prerequisites

### 1. Install Java JDK

**Option A: Download from Oracle**
- Visit: https://www.oracle.com/java/technologies/downloads/
- Download **Java SE 17 LTS** (or newer)
- Run the installer and follow the installation wizard
- Accept the defaults (Java will be installed to `C:\Program Files\Java\`)

**Option B: Download from Adoptium (OpenJDK)**
- Visit: https://adoptium.net/
- Download **Eclipse Temurin JDK 17 LTS** (or newer)
- Run the installer and follow the installation wizard

### 2. Verify Java Installation

After installation, verify Java is installed by opening a new Command Prompt or PowerShell and running:

```bash
java -version
javac -version
```

You should see output like:
```
java version "17.0.x" ...
javac 17.0.x
```

## Build Instructions

Once Java is installed, follow these steps to compile the application:

### Step 1: Navigate to Project Directory

```bash
cd c:\Users\jtsen\Documents\repos\CustomerNinja
```

### Step 2: Compile All Java Files

```bash
javac -cp src;lib\sqlite-jdbc.jar -d build src\com\customerninja2\*.java
```

**What this does:**
- `javac` - Java compiler
- `-cp src;lib\sqlite-jdbc.jar` - Classpath (source directory + SQLite driver)
- `-d build` - Output directory for compiled .class files
- `src\com\customerninja2\*.java` - All Java source files

**Expected output:**
- No error messages (silence is success)
- New `.class` files appear in `build\com\customerninja2\` directory

### Step 3: Verify Compilation

Check that the build directory has the compiled classes:

```bash
dir /s build\com\customerninja2\
```

You should see files like:
```
Pos.class
LoginPanel.class
InventoryPanel.class
... (and other .class files)
```

## Run Instructions

### Option 1: Run from Command Prompt (Simple)

```bash
cd c:\Users\jtsen\Documents\repos\CustomerNinja
java -cp build;lib\sqlite-jdbc.jar com.customerninja2.Pos
```

### Option 2: Run from PowerShell (Alternative)

```powershell
cd 'c:\Users\jtsen\Documents\repos\CustomerNinja'
java -cp "build;lib\sqlite-jdbc.jar" "com.customerninja2.Pos"
```

### What to Expect

The application window should appear with:
1. **CustomerNinja title** in large letters
2. **Login form** with Username and Password fields
3. **Menu bar** with File, Edit, and Help menus

## Test Login Credentials

The database (NinjaDB.db) should have test accounts. Try:

**For Employee Access:**
- Ask your database administrator for employee credentials
- Or check the NinjaConn.java initialization code

**For Manager Access:**
- Ask your database administrator for manager credentials
- Or check the database setup documentation

## Troubleshooting

### Problem: "javac is not recognized"
**Solution:** Java is not installed or not in PATH
- Install Java JDK first
- Restart Command Prompt/PowerShell after installation

### Problem: "Cannot find symbol" errors during compilation
**Solution:** Missing or wrong classpath
- Verify `lib\sqlite-jdbc.jar` exists
- Check file path uses correct separators (`;` not `:`)

### Problem: "Exception in thread" when running
**Solution:** Database or classpath issue
- Verify `NinjaDB.db` is in current directory
- Check classpath includes both `build` and `lib\sqlite-jdbc.jar`

### Problem: Application crashes on login
**Solution:** Database issue
- Verify `NinjaDB.db` file exists
- Try creating a new database or restoring from backup
- Check NinjaConn.java connection string

## Build Automation (Optional)

### Using Ant (Already Configured)

The project has `build.xml` configured for Ant:

```bash
ant compile
ant run
```

### Creating a Batch Script

Create `build-and-run.bat` in the project root:

```batch
@echo off
echo Building CustomerNinja...
javac -cp src;lib\sqlite-jdbc.jar -d build src\com\customerninja2\*.java
if errorlevel 1 (
    echo Compilation failed!
    exit /b 1
)
echo Build successful! Running application...
java -cp build;lib\sqlite-jdbc.jar com.customerninja2.Pos
```

Then just double-click the batch file to compile and run!

### Creating a PowerShell Script

Create `build-and-run.ps1` in the project root:

```powershell
Write-Host "Building CustomerNinja..." -ForegroundColor Green
javac -cp "src;lib\sqlite-jdbc.jar" -d build "src\com\customerninja2\*.java"

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit
}

Write-Host "Build successful! Running application..." -ForegroundColor Green
java -cp "build;lib\sqlite-jdbc.jar" "com.customerninja2.Pos"
```

Then run:
```powershell
.\build-and-run.ps1
```

## Next Steps

1. **Install Java JDK** (if not already done)
2. **Compile** using the javac command above
3. **Run** the application
4. **Test** with login credentials
5. **Explore** the application features

## References

- Java SE Downloads: https://www.oracle.com/java/technologies/downloads/
- Adoptium (OpenJDK): https://adoptium.net/
- SQLite JDBC: https://github.com/xerial/sqlite-jdbc
- CustomerNinja Documentation: See INDEX.md

---

**Status:** ✅ Code ready to compile and run  
**Requirements:** Java JDK 8+ (not currently installed)  
**Last Updated:** February 18, 2026

