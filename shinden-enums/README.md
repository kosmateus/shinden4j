# Shinden Enums

This module contains automatically generated enums for tags from the Shinden service.

## Structure

- `src/main/java/com/github/kosmateus/shinden/enums/tag/` - generated enum classes
- `src/main/resources/tags-translation.yaml` - translations for tags

## Generating Enums

### Automatic Generation (Maven Profile)

To update enums and translations (e.g., when Shinden adds new tags), run:

```bash
mvn clean compile -pl shinden-enums -am -Pgenerate-enums
```

**Note:** This command:

1. Fetches current data from the Shinden website
2. Generates new enum classes directly in `src/main/java`
3. Updates the translations file `src/main/resources/tags-translation.yaml`
4. Overwrites existing files

### Standard Build

The standard build uses already generated classes from `src/main/java` and does **not** run the generator:

```bash
mvn clean install
```

## Generated Enums

The following enums are automatically generated:

- `Studio` - animation studios
- `Genre` - anime genres
- `TargetGroup` - target groups
- `CharacterType` - character types
- `PlaceAndTime` - places and times of action
- `Other` - other tags
- `ProductionType` - production types
- `Publisher` - publishers
- `SourceMaterial` - source materials

## For Developers

Files in `src/main/java` and `src/main/resources` are version-controlled in Git, which means:

- Anyone can build the project without needing to run the generator
- You don't need access to the Shinden website during the build
- The history of tag changes is tracked in Git

Run the generator only when you want to update tags to the latest version from Shinden.

