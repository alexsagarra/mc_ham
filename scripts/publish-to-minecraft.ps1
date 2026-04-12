param(
    [string]$MinecraftDir = "$env:APPDATA\\.minecraft",
    [switch]$BuildFirst
)

$ErrorActionPreference = "Stop"

$projectRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$libsDir = Join-Path $projectRoot "build\\libs"

if ($BuildFirst) {
    $gradleBat = Join-Path $projectRoot "gradlew.bat"
    Push-Location $projectRoot
    try {
        & $gradleBat build
        if ($LASTEXITCODE -ne 0) {
            throw "Build fehlgeschlagen."
        }
    }
    finally {
        Pop-Location
    }
}

if (-not (Test-Path $libsDir)) {
    throw "build/libs wurde nicht gefunden. Bitte zuerst bauen."
}

$jar = Get-ChildItem -Path $libsDir -Filter "*.jar" |
    Where-Object { $_.Name -notmatch "(sources|javadoc|dev)" } |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1

if (-not $jar) {
    throw "Kein deploybares JAR in build/libs gefunden."
}

$modsDir = Join-Path $MinecraftDir "mods"
if (-not (Test-Path $modsDir)) {
    New-Item -ItemType Directory -Path $modsDir -Force | Out-Null
}

$target = Join-Path $modsDir $jar.Name
Copy-Item -Path $jar.FullName -Destination $target -Force

Write-Host "Kopiert: $($jar.FullName)"
Write-Host "Nach:     $target"
