#!/bin/bash

HOOKS_DIR="scripts/hooks"
GIT_HOOKS_DIR=".git/hooks"

if [ -d "$GIT_HOOKS_DIR" ]; then
  echo "Setting up pre-push hook..."
  cp "$HOOKS_DIR/pre-push" "$GIT_HOOKS_DIR/pre-push"
  chmod +x "$GIT_HOOKS_DIR/pre-push"
  echo "Pre-push hook installed successfully."
else
  echo "Error: .git directory not found. Are you in the root of the Git repository?"
fi