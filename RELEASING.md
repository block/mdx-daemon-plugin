# Releasing the MDX Daemon IDE Plugin

This doc explains how to cut a new version and publish it to:
- GitHub Releases (with the plugin ZIP attached)
- JetBrains Marketplace

## Prerequisites
Write access to this repo.

GitHub Actions secrets are set:

`JETBRAINS_MARKETPLACE_SQUARE_PLUGINS` – JetBrains Marketplace token.

The GitHub workflow Publish Plugin is present in `.github/workflows`.

You have a clean, up-to-date main branch locally:

```
git checkout main && git pull
```

## 1. Prepare the release
   Pick a semantic version: X.Y.Z (e.g., 0.0.3).

Update the project version

In the root build.gradle[.kts], set:

```groovy
version = 'X.Y.Z'
```
In plugin.xml, set:

```xml
<version>X.Y.Z</version>
```

## 2. Update the changelog
Add a ## X.Y.Z section with highlights.

## 3. Open a PR
Title: Release X.Y.Z.
Ensure CI (build + verify) is green.

## 4. Merge the PR
After merge, pull main locally:

```bash
git checkout main
git pull
```

## 5. Tag the release
The workflow triggers only on tags matching v*.*.*.

```bash 
# from up-to-date main
git tag vX.Y.Z
git push origin vX.Y.Z
```

(Optional)
Verify the tag points to the version bump

```bash
git show vX.Y.Z:build.gradle | grep "^version"
```

### Version Locations

Jetbrains Marketplace: https://plugins.jetbrains.com/plugin/27159-mdx-daemon/edit/versions

GitHub Releases: https://github.com/block/mdx-daemon-plugin/releases