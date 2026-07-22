# Implementation Plan - UI Refinement and Long Screenshot Share

This plan addresses UI overlap issues with system bars and implements a "Long Screenshot" sharing feature for the recipe screen.

## User Review Required

> [!IMPORTANT]
> The "Long Screenshot" feature will be implemented by rendering the recipe content into an off-screen Bitmap. This ensures the entire recipe (Ingredients and Instructions) is captured even if it exceeds the screen height.

> [!NOTE]
> We will need to add a `FileProvider` to the `AndroidManifest.xml` to safely share the generated image with other apps.

## Proposed Changes

### Edge-to-Edge UI Fixes

#### [MODIFY] [BottomNavBar.kt](file:///C:/Projects/android/shuksha/app/src/main/java/com/example/shuksha/presentation/BottomNavBar.kt)
- Add `Modifier.navigationBarsPadding()` to the outermost `Box` to prevent the bottom navigation bar from being covered by the system navigation bar.

#### [MODIFY] [RecipeDetailScreen.kt](file:///C:/Projects/android/shuksha/app/src/main/java/com/example/shuksha/presentation/RecipeDetailScreen.kt)
- Add `Modifier.statusBarsPadding()` to the top `Row` that contains the Back and Share buttons to prevent them from being covered by the status bar.

#### [MODIFY] [MainActivity.kt](file:///C:/Projects/android/shuksha/app/src/main/java/com/example/shuksha/MainActivity.kt)
- Ensure the `Scaffold` correctly handles insets and consumes them to avoid double-padding issues.

---

### Share as Long Screenshot

#### [NEW] [ScreenshotUtils.kt](file:///C:/Projects/android/shuksha/app/src/main/java/com/example/shuksha/util/ScreenshotUtils.kt)
- Implement a utility to:
    - Capture a Composable as a `Bitmap`.
    - Save the `Bitmap` to a temporary file.
    - Share the file using `Intent.ACTION_SEND` and `FileProvider`.
    - Save the `Bitmap` to the device gallery using `MediaStore`.

#### [MODIFY] [RecipeDetailScreen.kt](file:///C:/Projects/android/shuksha/app/src/main/java/com/example/shuksha/presentation/RecipeDetailScreen.kt)
- Wrap the recipe content in a `graphicsLayer` if necessary, or use a specific capture mechanism.
- Update the Share button logic to call the new screenshot utility.
- Add a "Save to Gallery" option if desired (e.g., via a small menu or a long-press on share).

#### [MODIFY] [AndroidManifest.xml](file:///C:/Projects/android/shuksha/app/src/main/AndroidManifest.xml)
- Add `<provider>` tag for `androidx.core.content.FileProvider`.

#### [NEW] [file_paths.xml](file:///C:/Projects/android/shuksha/app/src/main/res/xml/file_paths.xml)
- Define the paths for the `FileProvider`.

## Verification Plan

### Automated Tests
- Build the project to ensure no compilation errors.

### Manual Verification
- **Bottom NavBar:** Verify it is no longer covered by the mobile nav bar on various screen sizes/densities.
- **Recipe Detail Screen:** Verify the top buttons are below the status bar.
- **Share Recipe:**
    - Click share and verify a full-length image of the recipe is generated and shared.
    - Test sharing to social media apps.
    - Verify "Save to Gallery" functionality.
