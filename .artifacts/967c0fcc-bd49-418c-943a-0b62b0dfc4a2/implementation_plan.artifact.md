# Implementation Plan - Fix Drag and Drop Reordering

The drag-and-drop feature is currently non-functional because of a gesture conflict between the item's clickable area and the drag gesture handler.

## User Review Required

> [!IMPORTANT]
> The reordering will now be triggered by a long-press on any note **only after** selection mode has been activated. To enter selection mode, long-press a note once. Then, you can drag notes to reorder them.

## Proposed Changes

### UI Layer

#### [MODIFY] [NotesScreen.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/commonMain/kotlin/com/jayaram/tasktracker/NotesScreen.kt)

- **Fix Gesture Conflict**: I will disable the `onLongClick` in the `combinedClickable` modifier when `selectionMode` is true. This allows the parent `Card` to capture the long-press for dragging.
- **Improve Performance**: Add `key = { it.id }` to the `itemsIndexed` call in `LazyColumn`. This is critical for smooth animations during reordering.
- **Visual Improvements**:
    - Add `zIndex(1f)` to the item being dragged so it stays on top of others.
    - Improve the `swapNotes` logic to ensure local state and database stay in sync without jank.
- **Haptic Feedback**: Trigger haptic feedback when a drag starts and when items are swapped to give the user better physical cues.

## Verification Plan

### Manual Verification
1.  Open a folder with at least 3 notes.
2.  Long-press a note to enter selection mode.
3.  Once in selection mode, long-press a note again and hold.
4.  Drag the note up or down. Verify it moves visually and "swaps" with neighbors.
5.  Release the note.
6.  Go back to the folder list and return to the folder to verify the order was persisted.
