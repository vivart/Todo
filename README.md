![build](https://github.com/vivart/todo/actions/workflows/todo.yaml/badge.svg)

[![codecov](https://codecov.io/gh/vivart/Todo/branch/master/graph/badge.svg?token=9K673XE18U)](https://codecov.io/gh/vivart/Todo)

# Todo

It's an experimental repo insipired from https://commonsware.com/AndExplore. I experiment all new
android features on this repo.

This repo has features like

1. Modularization.
2. Jetpack architecture components

- Navigation
- ViewModel

3. Complete code is in Kotlin even for gradle also I am using kotlin dsl
4. Kotlin coroutines with stateflow.
5. Microbenchmark and Macrobenchmark
6. Hilt for dependency injection.
7. Retrofit with gson
8. Room
9. WorkManager
10. Add [Kover - Gradle plugin for Kotlin code coverage](https://github.com/Kotlin/kotlinx-kover).

Added support for VERSION_CATALOGS and TYPESAFE_PROJECT_ACCESSORS
> A version catalog is a list of dependencies, represented as dependency coordinates, that a user can pick from when declaring dependencies in a build script.
For example, instead of declaring a dependency using a string notation, the dependency coordinates can be picked from a version catalog:
https://docs.gradle.org/current/userguide/platforms.html

> Since Gradle 7, Gradle offers an experimental type-safe API for project dependencies.
https://docs.gradle.org/7.0/userguide/declaring_dependencies.html#sec:type-safe-project-accessors
