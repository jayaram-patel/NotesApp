# Replace Drag-and-Drop Reordering with Manual Up/Down Buttons

The glitchy drag-and-drop note reordering has been replaced with explicit "Move Up" and "Move Down" buttons that appear in selection mode. Additionally, unnecessary dependencies were removed.

## Changes Made

### [NotesScreen.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/commonMain/kotlin/com/jayaram/tasktracker/NotesScreen.kt)
- **Removed Drag Logic**: Deleted `draggedItemIndex`, `dragOffset`, and all related `pointerInput`, `graphicsLayer`, and `zIndex` modifiers.
- **Added Reorder Buttons**:
    - Introduced two `IconButton`s inside the note item's `Row` that are visible only in `selectionMode`.
    - Used the standard `Icons.Default.ArrowBack` icon, rotated 90° for "Up" and -90° for "Down", ensuring compatibility without the extended icons library.
    - Linked these buttons to the existing `swapNotes` function for stable reordering.
- **Cleanup**: Removed unused imports related to animations and gestures.

### [shared/build.gradle.kts](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/build.gradle.kts)
- **Dependency Removal**: Removed `compose.materialIconsExtended` to reduce the app's binary size, as all required icons are now sourced from the standard Material icon set.

## Verification Results

### Automated Tests
- **Gradle Sync**: Completed successfully after removing the dependency.
- **Compilation**: The project builds without errors.

### Manual Verification
1. **Enter Selection Mode**: Long-press a note to enter selection mode.
2. **Reorder**:
    - Tap the **↑** (rotated arrow) to move a note up.
    - Tap the **↓** (rotated arrow) to move a note down.
3. **Persistance**: Verified that the notes stay in their new positions after the swap (handled by the existing `noteRepository.updateNotePosition` logic).
4. **Edge Cases**:
    - The "Up" button is disabled for the first note.
    - The "Down" button is disabled for the last note.
