# CSS Guidelines for Gym Buddy

This document outlines the CSS standards and best practices for the Gym Buddy project. All team members using Claude Code should follow these guidelines when creating new pages and modifying existing ones.

---

## 🎯 Core Principle

**✅ NO INLINE STYLES** — All styling must be done through CSS classes defined in the centralized CSS files.

### Why?
- **Maintainability**: Styles are centralized and easy to find
- **Consistency**: Ensures uniform design across all pages
- **Reusability**: Classes can be composed and reused across pages
- **Performance**: Smaller HTML files, better caching
- **Collaboration**: Team members understand the design system

---

## 📁 CSS File Structure

```
static/css/
├── style.css            (Main import file - imports all modules)
├── common.css           (Variables, reset, typography, base styles)
├── layout.css           (Grid, flexbox, container layouts)
├── components.css       (Buttons, cards, forms, etc.)
├── pages.css            (Page-specific styles)
└── theme.css            (Utility classes & theme helpers) ← NEW
```

### File Responsibilities

- **common.css**: CSS variables, color scheme, base element styles
- **layout.css**: Layout utilities, grid systems, spacing
- **components.css**: Reusable UI components (buttons, cards, forms)
- **pages.css**: Styles specific to particular pages
- **theme.css**: Utility classes and helper classes for styling

---

## 🎨 Available CSS Variables

All colors are defined as CSS variables in `common.css`:

```css
:root {
  --iron:      #131313;  /* Darkest background */
  --concrete:  #1e1e1e;  /* Dark containers */
  --smoke:     #2a2a2a;  /* Form inputs, subtle backgrounds */
  --steel:     #3d3d3d;  /* Borders, dividers */
  --chalk:     #e9e5dc;  /* Primary text */
  --chalk-dim: #8a867e;  /* Secondary text, muted */
  --rust:      #c44b1b;  /* Brand color (accent) */
  --rust-hot:  #e0581f;  /* Hover state for rust */
  --amber:     #d4820a;  /* Secondary accent */
}
```

**Usage Example:**
```html
<!-- ❌ WRONG -->
<p style="color: #8a867e;">Text</p>

<!-- ✅ CORRECT -->
<p class="text-secondary">Text</p>
```

---

## 🛠️ Utility Classes Reference

### Display & Layout

```html
<!-- Flexbox -->
<div class="flex gap-md">              <!-- flex with gap -->
<div class="flex-col gap-lg">           <!-- flex column -->
<div class="flex-center">               <!-- centered flex -->
<div class="flex-between gap-md">       <!-- space-between flex -->

<!-- Display -->
<div class="hidden">                    <!-- display: none -->
<div class="inline-block">              <!-- display: inline-block -->
```

### Spacing (Margin & Padding)

**Margin Sizes**: `sm` (8px), `md` (12px), `lg` (16px), `xl` (20px), `2xl` (24px), `3xl` (32px)

```html
<!-- Margins -->
<div class="mt-lg">                     <!-- margin-top: 16px -->
<div class="mb-2xl">                    <!-- margin-bottom: 24px -->
<div class="ml-md mr-lg">               <!-- margin-left/right -->

<!-- Padding -->
<div class="pt-xl pb-2xl">              <!-- padding-top/bottom -->
<div class="px-lg">                     <!-- padding-left/right -->
```

### Text Utilities

```html
<!-- Alignment -->
<div class="text-center">
<div class="text-left">
<div class="text-right">

<!-- Colors -->
<p class="text-primary">              <!-- var(--chalk) -->
<p class="text-secondary">             <!-- var(--chalk-dim) -->
<p class="text-rust">                  <!-- var(--rust) -->
<p class="text-success">               <!-- #51cf66 -->
<p class="text-error">                 <!-- #ff6b6b -->

<!-- Size & Weight -->
<span class="font-sm">                 <!-- 13px -->
<span class="font-md">                 <!-- 14px -->
<span class="font-lg">                 <!-- 15px -->
<span class="font-bold">               <!-- font-weight: 700 -->
<span class="font-condensed">          <!-- Barlow Condensed -->

<!-- Other -->
<a class="no-decoration">              <!-- text-decoration: none -->
<div class="no-margin">                <!-- margin: 0 -->
```

### Borders

```html
<div class="border-bottom">            <!-- border-bottom: 1px solid var(--steel) -->
<div class="border-top">               <!-- border-top: 1px solid var(--steel) -->
<div class="border-bottom-thick">      <!-- 2px solid border -->
```

### Width & Size

```html
<div class="w-full">                   <!-- width: 100% -->
<div class="w-auto">                   <!-- width: auto -->
<div class="h-60">                     <!-- 60x60px (avatar size) -->
<div class="h-72">                     <!-- 72x72px -->
```

---

## 👤 Avatar Class

```html
<!-- Large avatar (72x72) -->
<div class="avatar">
  <span>J</span>  <!-- initials or can use <img> -->
</div>

<!-- Small avatar (60x60) -->
<div class="avatar avatar-sm">
  <span>J</span>
</div>
```

---

## 📝 Form Styles

All form elements should use the standardized classes:

```html
<div class="form-group">
  <label for="name" class="form-label">Username</label>
  <input type="text" id="name" class="form-input">
  <span class="form-small">Minimum 6 characters</span>
</div>

<div class="form-group">
  <label for="bio" class="form-label">Bio</label>
  <textarea id="bio" class="form-textarea"></textarea>
</div>

<div class="form-group">
  <label for="role" class="form-label">Role</label>
  <select id="role" class="form-select">
    <option>Select...</option>
  </select>
</div>
```

---

## 💬 Messages & Alerts

```html
<!-- Error message (always visible) -->
<div class="error-message">
  Something went wrong!
</div>

<!-- Success message (always visible) -->
<div class="success-message">
  Changes saved successfully!
</div>

<!-- Togglable message (use .show class to display) -->
<span id="profile-msg" class="msg"></span>

<!-- In JavaScript: -->
<script>
  const el = document.getElementById('profile-msg');
  el.className = 'msg success';  // or 'msg error'
  el.textContent = 'Saved!';
  el.style.display = 'inline-block';
  setTimeout(() => { el.style.display = 'none'; }, 4000);
</script>
```

---

## 🎯 Section & Container Patterns

```html
<!-- Standard section (with bottom spacing & border) -->
<div class="section">
  <p class="section-title">Profile Info</p>
  <!-- content here -->
</div>

<!-- Button row (aligned with gap) -->
<div class="btn-row">
  <button class="btn">Save</button>
  <button class="btn btn-secondary">Cancel</button>
</div>

<!-- Navigation footer -->
<div class="nav-footer">
  <a href="/profile" class="btn btn-secondary">Back to Profile</a>
  <form action="/logout" method="post">
    <button type="submit" class="btn">Logout</button>
  </form>
</div>
```

---

## ✍️ Complete Page Template

When creating a new page, use this structure:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GymBuddy - Page Name</title>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <div class="container">
        <h1>Page Title</h1>
        <p class="subtitle">Subtitle text</p>

        <!-- Use classes instead of inline styles -->
        <div class="section">
            <p class="section-title">Section Name</p>
            <!-- Content goes here -->
        </div>

        <!-- Navigation -->
        <div class="nav-footer">
            <a th:href="@{/profile}" class="btn btn-secondary">Back</a>
        </div>
    </div>

    <!-- No inline styles in <script> either -->
    <script th:src="@{/js/api.js}"></script>
</body>
</html>
```

---

## ❌ What NOT to Do

```html
<!-- ❌ WRONG: Inline styles -->
<div style="margin-top: 24px; display: flex; gap: 12px; color: #8a867e;">
  Content
</div>

<!-- ✅ CORRECT: Use classes -->
<div class="mt-2xl flex gap-md text-secondary">
  Content
</div>

<!-- ❌ WRONG: Hardcoded colors -->
<button style="background: #c44b1b; color: white;">Click</button>

<!-- ✅ CORRECT: Use existing button class -->
<button class="btn">Click</button>

<!-- ❌ WRONG: Inline layout styles -->
<div style="display: flex; justify-content: center; align-items: center; gap: 16px;">
  Item
</div>

<!-- ✅ CORRECT: Use utility classes -->
<div class="flex-center gap-lg">
  Item
</div>
```

---

## 📋 Checklist Before Creating a Page

- [ ] No inline `style` attributes in HTML
- [ ] All colors use CSS variables (e.g., `var(--rust)`)
- [ ] Spacing uses utility classes (`mt-lg`, `mb-2xl`, etc.)
- [ ] Form elements use `.form-input`, `.form-label`, `.form-group`
- [ ] Buttons use `.btn` or `.btn-secondary` classes
- [ ] Messages use `.error-message` or `.success-message` classes
- [ ] Section titles use `.section-title` class
- [ ] Navigation uses `.nav-footer` class
- [ ] All navigation links use `th:href="@{/path}"` (Thymeleaf syntax)
- [ ] CSS is imported as `th:href="@{/css/style.css}"`

---

## 🚀 Adding New Utilities

If you need a new utility class that doesn't exist:

1. **Add it to `theme.css`** (if it's a general utility)
2. **Or add it to `components.css`** (if it's a component-specific style)
3. **Document it here** in this guideline document
4. **Inform the team** in Slack/Discord so everyone knows about it

Example of adding a new utility:

```css
/* In theme.css */
.opacity-low {
  opacity: 0.6;
}

.shadow-md {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
```

Then document it in this file and use it:

```html
<div class="opacity-low shadow-md">
  Content with reduced opacity and shadow
</div>
```

---

## 📚 Fonts & Typography

The project uses two Google Fonts:

- **Barlow**: Body text, regular content
- **Barlow Condensed**: Headings, titles, labels

These are already loaded in `common.css`. Use the `.font-condensed` class to apply it:

```html
<!-- Automatically uses Barlow -->
<p>This is body text</p>

<!-- Use Barlow Condensed -->
<h2 class="font-condensed">Heading</h2>
<label class="font-condensed font-bold">Label</label>
```

---

## 🔍 Tips for Maintainability

1. **Prefer utility classes over custom CSS** — Use existing utilities before writing new styles
2. **Use semantic class names** — `profile-header` instead of `box-1`
3. **Keep specificity low** — Avoid nested selectors when possible
4. **Document new patterns** — If you add a new component or pattern, update this guide
5. **Reuse components** — Don't recreate buttons, cards, forms that already exist

---

## 💡 When to Add New Styles

**Add to `theme.css`:**
- General utility classes (spacing, text, display helpers)
- Reusable patterns (form groups, message styles, containers)

**Add to `components.css`:**
- Styled UI components (buttons, cards, modals, etc.)
- Variations of existing components

**Add to `pages.css`:**
- Page-specific customizations only
- Overrides for particular pages

---

## 🤝 Questions or Issues?

If you need help with CSS or want to suggest improvements to this guide:

1. Check if a utility class already exists in `theme.css`
2. Review this document for similar patterns
3. Ask the team in the chat

Thank you for maintaining consistent, clean code! 💪
