#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
GRADLEW="$PROJECT_ROOT/gradlew"
PUBLISH_SCRIPT="$PROJECT_ROOT/scripts/publish-to-minecraft.ps1"

usage() {
  cat <<'EOF'
MC Hammer helper commands

Usage:
  ./scripts/go.sh <command>

Commands:
  build         Gradle build
  run           Gradle runClient
  server        Gradle runServer
  copy          Copy latest JAR to Minecraft mods folder
  build-copy    Build and copy in one step
  clean         Gradle clean
  jar           Show latest deployable JAR in build/libs
  help          Show this help

Optional environment variables:
  MINECRAFT_DIR Path to .minecraft directory
EOF
}

run_gradle() {
  local task="$1"
  (
    cd "$PROJECT_ROOT"
    "$GRADLEW" "$task"
  )
}

find_powershell() {
  if command -v powershell.exe >/dev/null 2>&1; then
    echo "powershell.exe"
    return 0
  fi

  if command -v pwsh >/dev/null 2>&1; then
    echo "pwsh"
    return 0
  fi

  echo "PowerShell not found (required for copy/build-copy)." >&2
  return 1
}

copy_to_mods() {
  local ps
  ps="$(find_powershell)"

  local args=(-NoProfile -ExecutionPolicy Bypass -File "$PUBLISH_SCRIPT")

  if [[ -n "${MINECRAFT_DIR:-}" ]]; then
    args+=( -MinecraftDir "$MINECRAFT_DIR" )
  fi

  if [[ "${1:-}" == "with-build" ]]; then
    args+=( -BuildFirst )
  fi

  "$ps" "${args[@]}"
}

show_latest_jar() {
  local libs_dir="$PROJECT_ROOT/build/libs"

  if [[ ! -d "$libs_dir" ]]; then
    echo "build/libs not found. Run './scripts/go.sh build' first." >&2
    return 1
  fi

  local jar
  jar="$(ls -t "$libs_dir"/*.jar 2>/dev/null \
    | grep -Ev '(sources|javadoc|dev)\\.jar$' \
    | head -n 1)"

  if [[ -z "$jar" ]]; then
    echo "No deployable JAR found in build/libs." >&2
    return 1
  fi

  echo "$jar"
}

cmd="${1:-help}"

case "$cmd" in
  build)
    run_gradle build
    ;;
  run)
    run_gradle runClient
    ;;
  server)
    run_gradle runServer
    ;;
  copy)
    copy_to_mods
    ;;
  build-copy)
    copy_to_mods with-build
    ;;
  clean)
    run_gradle clean
    ;;
  jar)
    show_latest_jar
    ;;
  help|-h|--help)
    usage
    ;;
  *)
    echo "Unknown command: $cmd" >&2
    echo
    usage
    exit 1
    ;;
esac
