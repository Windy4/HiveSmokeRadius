# Hive Smoke Radius

Server-side Fabric mod for Minecraft **1.21.x** (built against **1.21.11**)
that widens vanilla's beehive "smoked out" mechanic.

## What it does

Vanilla only calms bees (no anger when you harvest honey/honeycomb or break the
hive) when a lit campfire sits in the **exact column directly below** the
hive/bee nest. This mod extends that check to the 8 horizontally
adjacent/diagonal columns (N, S, E, W, NE, NW, SE, SW) around the hive's own
column. A lit campfire whose smoke rises next to the hive now counts too.

Everything else stays vanilla:

- Unlit campfires still don't count.
- Vanilla's per-column obstruction rules (blocks that stop smoke) still apply,
  just per candidate column.
- General bee aggro (stinging players who hit bees, non-harvest disturbances)
  is untouched — only the harvest/break smoking check is widened.

## How it works

The vanilla smoked-out check is `CampfireBlock.isLitCampfireInRange(world, pos)`
(Yarn mappings; Mojmap `isSmokeyPos`). Every beehive-smoking decision goes
through it: `BeehiveBlockEntity.isSmoked()` (anger on hive break) and
`BeehiveBlock`'s use-with-item handler (the shears/glass-bottle harvest path).

A single mixin injects at that method's return: when the vanilla single-column
scan comes up empty, it re-runs the same vanilla scan on the 8 neighboring
columns (with a reentrancy guard so those inner calls stay single-column).
Vanilla's downward-scan depth and smoke-obstruction logic is reused untouched
per column. This method's signature is stable across the 1.21.x line, so the
one jar loads on any 1.21.x server (boot-verified on 1.21.1, 1.21.4, and
1.21.11).

The mod has no Fabric API dependency — only Fabric Loader (>= 0.16).

## Building

```sh
./gradlew build
```

The mod jar lands in `build/libs/`. Drop it in the server's `mods/` folder.
It also works in single player (the integrated server runs the mixins); clients
without the mod can join a modded server normally.
