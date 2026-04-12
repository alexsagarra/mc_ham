param(
    [ValidateSet('build','run','server','copy','build-copy','clean','jar','help')]
    [string]$Command = 'help',
    [string]$MinecraftDir
)

$ErrorActionPreference = 'Stop'

$projectRoot = Resolve-Path (Join-Path $PSScriptRoot '..')
$gradleBat = Join-Path $projectRoot 'gradlew.bat'
$publishScript = Join-Path $projectRoot 'scripts\publish-to-minecraft.ps1'
$libsDir = Join-Path $projectRoot 'build\libs'

function Show-Help {
    @"
MC Hammer helper commands

Usage:
  .\scripts\go.ps1 -Command <command>

Commands:
  build       Gradle build
  run         Gradle runClient
  server      Gradle runServer
  copy        Copy latest JAR to Minecraft mods folder
  build-copy  Build and copy in one step
  clean       Gradle clean
  jar         Show latest deployable JAR in build/libs
  help        Show this help

Optional:
  -MinecraftDir "D:\Games\Minecraft\.minecraft"
"@
}

function Run-Gradle {
    param([string]$Task)

    Push-Location $projectRoot
    try {
        & $gradleBat $Task
        if ($LASTEXITCODE -ne 0) {
            throw "Gradle task failed: $Task"
        }
    }
    finally {
        Pop-Location
    }
}

function Copy-ToMods {
    param([switch]$BuildFirst)

    $publishParams = @{}
    if ($BuildFirst) {
        $publishParams.BuildFirst = $true
    }

    if ($MinecraftDir) {
        $publishParams.MinecraftDir = $MinecraftDir
    }

    & $publishScript @publishParams
    if ($LASTEXITCODE -ne 0) {
        throw 'Copy to mods failed.'
    }
}

function Show-LatestJar {
    if (-not (Test-Path $libsDir)) {
        throw 'build/libs not found. Run build first.'
    }

    $jar = Get-ChildItem -Path $libsDir -Filter '*.jar' |
        Where-Object { $_.Name -notmatch '(sources|javadoc|dev)' } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    if (-not $jar) {
        throw 'No deployable JAR found in build/libs.'
    }

    $jar.FullName
}

switch ($Command) {
    'build' { Run-Gradle 'build' }
    'run' { Run-Gradle 'runClient' }
    'server' { Run-Gradle 'runServer' }
    'copy' { Copy-ToMods }
    'build-copy' { Copy-ToMods -BuildFirst }
    'clean' { Run-Gradle 'clean' }
    'jar' { Show-LatestJar }
    'help' { Show-Help }
}
