# ExpenseTracker (HCI_Group3) - Enhanced

This branch contains multiple UI and architecture improvements to the original ExpenseTracker app. The goal was to modernize the authentication flow, add a local database backend, improve visual styling, and make the auth screens swipeable with simple animations and responsive layouts.

What I changed

- Replaced separate `LoginActivity`/`SignupActivity` flow with a swipeable `AuthActivity` using `ViewPager2` and `TabLayout`.
- Implemented `LoginFragment` and `SignupFragment` to host the auth forms.
- Added Room local database (`AppDatabase`, `User` entity, `UserDao`) to persist registered users.
- Switched auth logic to use Room + simple SHA-256 password hashing (for demo only; in production use a proper salted hashing algorithm like bcrypt).
- Added a modern color palette and button styles in `res/values/colors.xml` and `res/values/themes.xml`.
- Added ViewPager2 and Room dependencies in `app/build.gradle.kts`.

Tech stack

- Android (Kotlin)
- AndroidX: ViewPager2, Fragment KTX, Lifecycle
- Room for local persistence
- Coroutines for background operations
- Material Components for UI

Files added/modified (high level)

- app/src/main/java/com/example/expensetracker/
  - AuthActivity.kt
  - AuthPagerAdapter.kt
  - LoginFragment.kt
  - SignupFragment.kt
  - data/AppDatabase.kt
  - data/User.kt
  - data/UserDao.kt
- app/src/main/res/layout/
  - activity_auth.xml
  - fragment_login.xml
  - fragment_signup.xml
- app/src/main/res/values/colors.xml (new)
- app/src/main/res/values/themes.xml (updated)
- app/build.gradle.kts (dependencies updated)
- README.md (this file)

How to build & run

1. Open the project in Android Studio.
2. Let Gradle sync. If you see kapt errors, run "Build > Clean Project" then "Build > Rebuild Project".
3. Run the app on an emulator or device.

Notes and next steps

- Password hashing here uses plain SHA-256 for demonstration — use a secure salted hash (e.g., Argon2/bcrypt) in production.
- I added a Room database with only the `users` table; transactions and analytics can be migrated from the original JSON/Gson approach into Room entities.
- Animations: basic swipe and TabLayout indicate the flow. If you want richer motion (Lottie, shared element transitions), I can add them next.

Git push commands

Use the following commands to push the changes to your GitHub repository (replace origin/url and branch as needed):

```bash
git add .
git commit -m "Enhance auth UI: swipeable ViewPager2 auth, add Room DB, themes and layouts"
git branch -M main
git remote add origin <YOUR_GIT_REMOTE_URL>
git push -u origin main
```

If you already have a remote, skip the `git remote add` step.

If you'd like, I can continue by converting existing transaction storage to Room, add animations (Lottie), and polish the home screen UI.

