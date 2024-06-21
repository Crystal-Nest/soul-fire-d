# Change Log

All notable changes to the "soul-fire-d" Minecraft mod will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Crystal Nest Semantic Versioning](https://crystalnest.it/#/versioning).

## [Unreleased]
- Nothing new.

## [v4.0.2] - 2024/06/21

- Fixed [#45](https://github.com/Crystal-Nest/soul-fire-d/issues/45), DDFires datapacks not working.
- Fixed [#46](https://github.com/Crystal-Nest/soul-fire-d/issues/46), flaming arrows hitting entities cause game crash.

## [v4.0.1] - 2024/06/20

- Fixed in-game icon.

## [v4.0.0] - 2024/06/20

- Stable release.
- Fix mod version wrongly including Minecraft version too.

## [v3.4.3-beta] - 2024/06/18

- Updated Cobweb dependency version to `1.0.0`.

## [v3.4.2-beta] - 2024/06/18

- Fixed possible NPE when trying to get a fire, a fire property, or a fire component.

## [v3.4.1-beta] - 2024/06/16

- Removed need to pass the particle type when registering torch blocks.
- Added method overloads to register subclasses of the custom fire components.
- More Javadoc.

## [v3.4.0-beta] - 2024/06/15

- `DynamicBlockEntityType` is now stricter and accepts only subclasses of `CustomCampfireBlockEntity`.
- Merged registration for `CustomTorchBlock` and `CustomWallTorchBlock`.
- Updated `FireClientManager` to match the new registering standard when registering fires.
- Made `FireBuilder` and `FireComponent` inner classes of `Fire`, and renamed them respectively to `Builder` and `Component` (accessible as `Fire.Builder` and `Fire.Component`).
- Added tons of Javadoc, either missing or outdated.

## [v3.3.5-alpha] - 2024/06/15

- Setting fire damage to 0 now entirely prevents calls to hurt/heal entity methods.
- Added overload for `setBehavior` as a shorthand for custom behaviors that don't prevent the default hurt/heal behavior.
- Added system of `FireComponent`s to register and retrieve fire related blocks, items, particles, and enchantments.
- Changed many fire component registration methods to automatically retrieve the other required components.
- Fixed custom campfires cooking function breaking when re-entering a world.
- Removed useless `CustomCampfireRenderer`.

## [v3.3.4-alpha] - 2024/06/13

- Improve `FireManager` and streamline related code.
- Added new more general methods to retrieve fire properties and removed obsolete ones.
- Fixed and improve use of `CustomCampfireBlockEntity` and its related type.
- Made registration of `CustomCampfireBlockEntity` automatic and removed manual one.
- Allowed to override static behaviors of `CustomCampfireBlockEntity` by overriding specific methods in `CustomCampfireBlock`.
- Fixed NPE when lighting up a fire if there was at least one fire without a registered source block.

## [v3.3.3-alpha] - 2024/06/11

- Added custom fire behavior.
- Added `canRainDouse` flag.
- Removed method overloads in `FireManager` that accepted a pair of strings instead of a `ResourceLocation` to retrieve a fire property.

## [v3.3.2-alpha] - 2024/06/09

- Add use of Cobweb unified registration system to the fire-related stuff registration API.

## [v3.3.1-alpha] - 2024/06/07

- Added API for registering custom fire blocks and campfire blocks, along with required block entities and renderers.
- Added API for registering custom campfire items.
- Added API for registering custom lantern blocks and items.
- Added API for registering custom torch blocks and items, along with required particles.
- Added new Fire property `light` to set the light level of the Fire.

## [v3.3.0-alpha] - 2024/06/04

- Rewrote to new standards with multiloader environment.

## Legacy

<details>
  <summary>Click to expand</summary>

  ### [1.20.4-3.2.1.0] - 2024/01/13
  - Ported to 1.20.4.
  - Fix `FireManager.getCampfireBlock()` returning the source block rather than the campfire block.

  ### [1.20.2-3.2.1.0] - 2023/11/17
  - Add [#32](https://github.com/crystal-nest/soul-fire-d/issues/32).
  - Add new tweaking option for enchantments, refer to the [wiki](https://github.com/crystal-nest/soul-fire-d/wiki/Registering-your-Fire#tweaking-enchantments) for more info.
  - Fix `FireManager.getModIs()` returning FireIds rather than ModIds.

  ### [1.20.1-3.2.1.0] - 2023/12/20
  - Add [#32](https://github.com/crystal-nest/soul-fire-d/issues/32).
  - Add new tweaking option for enchantments, refer to the [wiki](https://github.com/crystal-nest/soul-fire-d/wiki/Registering-your-Fire#tweaking-enchantments) for more info.
  - Fix `FireManager.getModIs()` returning FireIds rather than ModIds.

  ### [1.19.4-3.2.1.0] - 2023/11/17
  - Add [#32](https://github.com/crystal-nest/soul-fire-d/issues/32).
  - Add new tweaking option for enchantments, refer to the [wiki](https://github.com/crystal-nest/soul-fire-d/wiki/Registering-your-Fire#tweaking-enchantments) for more info.
  - Fix `FireManager.getModIs()` returning FireIds rather than ModIds.

  ### [1.19.2-3.2.1.0] - 2023/11/17
  - Add [#32](https://github.com/crystal-nest/soul-fire-d/issues/32).
  - Add new tweaking option for enchantments, refer to the [wiki](https://github.com/crystal-nest/soul-fire-d/wiki/Registering-your-Fire#tweaking-enchantments) for more info.
  - Fix `FireManager.getModIs()` returning FireIds rather than ModIds.

  ### [1.18.2-3.2.1.0] - 2023/11/17
  - Add [#32](https://github.com/crystal-nest/soul-fire-d/issues/32).
  - Add new tweaking option for enchantments, refer to the [wiki](https://github.com/crystal-nest/soul-fire-d/wiki/Registering-your-Fire#tweaking-enchantments) for more info.
  - Fix `FireManager.getModIs()` returning FireIds rather than ModIds.

  ### [1.16.5-3.2.1.0] - 2023/11/17
  - Add [#32](https://github.com/crystal-nest/soul-fire-d/issues/32).
  - Add new tweaking option for enchantments, refer to the [wiki](https://github.com/crystal-nest/soul-fire-d/wiki/Registering-your-Fire#tweaking-enchantments) for more info.
  - Fix `FireManager.getModIs()` returning FireIds rather than ModIds.

  ### [1.20.2-3.2.0.1] - 2023/10/05
  - Fix [#28](https://github.com/crystal-nest/soul-fire-d/issues/28).
  - Port to 1.20.2

  ### [1.20.1-3.2.0.1-final] - 2023/10/05
  - Fix [#28](https://github.com/crystal-nest/soul-fire-d/issues/28).

  ### [1.19.4-3.2.0.1] - 2023/10/05
  - Fix [#28](https://github.com/crystal-nest/soul-fire-d/issues/28).

  ### [1.19.3-3.2.0.1-final] - 2023/10/05
  - Fix [#28](https://github.com/crystal-nest/soul-fire-d/issues/28).

  ### [1.19.2-3.2.0.1] - 2023/10/05
  - Fix [#28](https://github.com/crystal-nest/soul-fire-d/issues/28).

  ### [1.18.2-3.2.0.1] - 2023/10/05
  - Fix [#28](https://github.com/crystal-nest/soul-fire-d/issues/28).

  ### [1.16.5-3.2.0.1] - 2023/10/05
  - Fix [#28](https://github.com/crystal-nest/soul-fire-d/issues/28).

  ### [1.20.1-3.2.0.0] - 2023/07/04
  - Port to 1.20.1
  - Add Data Driven Fires, see [#4](https://github.com/crystal-nest/soul-fire-d/issues/4) and the related [Wiki page](https://github.com/crystal-nest/soul-fire-d/wiki/Data-Driven-Fires).
  - Added new configuration options for enchantments, see [#25](https://github.com/crystal-nest/soul-fire-d/issues/25).

  ### [1.19.4-3.2.0.0] - 2023/07/04
  - Add Data Driven Fires, see [#4](https://github.com/crystal-nest/soul-fire-d/issues/4) and the related [Wiki page](https://github.com/crystal-nest/soul-fire-d/wiki/Data-Driven-Fires).
  - Added new configuration options for enchantments, see [#25](https://github.com/crystal-nest/soul-fire-d/issues/25).
  - This version should be final, unless bugfixes.

  ### [1.19.3-3.2.0.0] - 2023/07/04
  - Add Data Driven Fires, see [#4](https://github.com/crystal-nest/soul-fire-d/issues/4) and the related [Wiki page](https://github.com/crystal-nest/soul-fire-d/wiki/Data-Driven-Fires).
  - Added new configuration options for enchantments, see [#25](https://github.com/crystal-nest/soul-fire-d/issues/25).
  - This version should be final, unless bugfixes.

  ### [1.19.2-3.2.0.0] - 2023/07/04
  - Add Data Driven Fires, see [#4](https://github.com/crystal-nest/soul-fire-d/issues/4) and the related [Wiki page](https://github.com/crystal-nest/soul-fire-d/wiki/Data-Driven-Fires).
  - Added new configuration options for enchantments, see [#25](https://github.com/crystal-nest/soul-fire-d/issues/25).
  - This version should be final, unless bugfixes.

  ### [1.18.2-3.2.0.0] - 2023/07/04
  - Add Data Driven Fires, see [#4](https://github.com/crystal-nest/soul-fire-d/issues/4) and the related [Wiki page](https://github.com/crystal-nest/soul-fire-d/wiki/Data-Driven-Fires).
  - Added new configuration options for enchantments, see [#25](https://github.com/crystal-nest/soul-fire-d/issues/25).

  ### [1.16.5-3.2.0.0] - 2023/07/04
  - Add Data Driven Fires, see [#4](https://github.com/crystal-nest/soul-fire-d/issues/4) and the related [Wiki page](https://github.com/crystal-nest/soul-fire-d/wiki/Data-Driven-Fires).
  - Added new configuration options for enchantments, see [#25](https://github.com/crystal-nest/soul-fire-d/issues/25).
  - Fabric only: added a new server lifecycle event for datapack syncing, backport of the same Fabric API event of later versions.

  ### [1.19.4-3.1.0.0] - 2023/03/18
  - Ported to 1.19.4
  - Changed in-game mod picture.
  - Reworked how damage sources are registered due to breaking changes from Minecraft. See the [Changes since 1.19.4](https://github.com/crystal-nest/soul-fire-d/wiki/Changes-since-1.19.4) wiki section for more details.
  - Fixed readme.

  ### [1.19.3-3.1.0.0] - 2023/03/18
  - Changed in-game mod picture.
  - Improved unicity for registering damage sources.
  - Fixed readme.

  ### [1.19.2-3.1.0.0-final] - 2023/03/18
  - Changed in-game mod picture.
  - Improved unicity for registering damage sources.
  - Fixed readme.

  ### [1.18.2-3.1.0.0] - 2023/03/18
  - Changed in-game mod picture.
  - Improved unicity for registering damage sources.
  - Fixed readme.

  ### [1.16.5-3.1.0.0] - 2023/03/18
  - Changed in-game mod picture.
  - Improved unicity for registering damage sources.
  - Fixed readme.

  ### [1.19.3-3.0.1.0] - 2023/03/11
  - Improved API.

  ### [1.19.2-3.0.1.0] - 2023/03/11
  - Improved API.

  ### [1.18.2-3.0.1.0] - 2023/03/11
  - Improved API.

  ### [1.16.5-3.0.1.0] - 2023/03/11
  - Improved API.

  ### [1.19.3-3.0.0.1] - 2023/03/04
  - Updated build files to publish on Modrinth ([#18](https://github.com/crystal-nest/soul-fire-d/issues/18)).
  - Fixed [#15](https://github.com/crystal-nest/soul-fire-d/issues/15).

  ### [1.19.2-3.0.0.1] - 2023/03/04
  - Updated build files to publish on Modrinth ([#18](https://github.com/crystal-nest/soul-fire-d/issues/18)).
  - Fixed [#15](https://github.com/crystal-nest/soul-fire-d/issues/15).

  ### [1.18.2-3.0.0.1] - 2023/03/04
  - Updated build files to publish on Modrinth ([#18](https://github.com/crystal-nest/soul-fire-d/issues/18)).
  - Fixed [#15](https://github.com/crystal-nest/soul-fire-d/issues/15).

  ### [1.16.5-3.0.0.1] - 2023/03/04
  - Updated build files to publish on Modrinth ([#18](https://github.com/crystal-nest/soul-fire-d/issues/18)).
  - Fixed [#15](https://github.com/crystal-nest/soul-fire-d/issues/15).

  ### [1.19.3-3.0.0.0] - 2023/01/10
  - Substantially improved the API and registration for custom Fires.
  - Added configuration options to enable (default) or disable Soul Fire'd enchantments.
  - Improved compatibility with Ensorcellation.
  - Fixed a bug in Fabric that would grant to find Soul Flame in most bastion chests.
  - Changed Soul Fire id, from `"soulfired:soul_fire"` to `"minecraft:soul_fire"`, with subsequent changes to other ids (e.g. enchantments).

  ### [1.19.2-3.0.0.0] - 2023/01/10
  - Substantially improved the API and registration for custom Fires.
  - Added configuration options to enable (default) or disable Soul Fire'd enchantments.
  - Improved compatibility with Ensorcellation.
  - Fixed a bug in Fabric that would grant to find Soul Flame in most bastion chests.
  - Changed Soul Fire id, from `"soulfired:soul_fire"` to `"minecraft:soul_fire"`, with subsequent changes to other ids (e.g. enchantments).

  ### [1.18.2-3.0.0.0] - 2023/01/10
  - Substantially improved the API and registration for custom Fires.
  - Added configuration options to enable (default) or disable Soul Fire'd enchantments.
  - Improved compatibility with Ensorcellation.
  - Fixed a bug in Fabric that would grant to find Soul Flame in most bastion chests.
  - Changed Soul Fire id, from `"soulfired:soul_fire"` to `"minecraft:soul_fire"`, with subsequent changes to other ids (e.g. enchantments).

  ### [1.16.5-3.0.0.0] - 2023/01/10
  - Substantially improved the API and registration for custom Fires.
  - Added configuration options to enable (default) or disable Soul Fire'd enchantments.
  - Improved compatibility with Ensorcellation.
  - Fixed a bug in Fabric that would grant to find Soul Flame in most bastion chests.
  - Changed Soul Fire id, from `"soulfired:soul_fire"` to `"minecraft:soul_fire"`, with subsequent changes to other ids (e.g. enchantments).

  ### [1.19.3-2.0.0.1] - 2023/01/02
  - Improved compatibility, especially with Nyf's Spiders.

  ### [1.19.2-2.0.0.1] - 2023/01/02
  - Improved compatibility, especially with Nyf's Spiders.

  ### [1.18.2-2.0.0.1] - 2023/01/02
  - Improved compatibility, especially with Nyf's Spiders.

  ### [1.16.5-2.0.0.1] - 2023/01/02
  - Improved compatibility, especially with Spiders 2.0.

  ### [1.19.3-2.0.0.0] - 2023/01/01
  - Ported to 1.19.3.

  ### [1.19.2-2.0.0.0] - 2023/01/01
  - Changed default value of isTreasure for fire typed enchantments.
  - Changed rarity of soul fired enchantments: now they can be found using the enchantment table, albeit rarely.
  - Added a chance for soul flame enchantment to be part of a bastion loot.
  - Fixed a bug with blazes.

  ### [1.18.2-2.0.0.0] - 2023/01/01
  - Changed default value of isTreasure for fire typed enchantments.
  - Changed rarity of soul fired enchantments: now they can be found using the enchantment table, albeit rarely.
  - Added a chance for soul flame enchantment to be part of a bastion loot.
  - Fixed a bug with blazes.

  ### [1.16.5-2.0.0.0] - 2023/01/01
  - Changed default value of isTreasure for fire typed enchantments.
  - Changed rarity of soul fired enchantments: now they can be found using the enchantment table, albeit rarely.
  - Added a chance for soul flame enchantment to be part of a bastion loot.
  - Fixed a bug with blazes.

  ### [1.19.2-1.1.0.0] - 2022/09/24
  - Fixed compatibility with Malum (and probably some other mods).
  - Fixed a bug that would prevent soul flamed arrows to catch normal fire in lava.
  - Improved logic and expanded API.

  ### [1.19.1-1.1.0.0] - 2022/09/24
  - Fixed compatibility with Malum (and probably some other mods).
  - Fixed a bug that would prevent soul flamed arrows to catch normal fire in lava.
  - Improved logic and expanded API.

  ### [1.19-1.1.0.0] - 2022/09/24
  - Fixed compatibility with Malum (and probably some other mods).
  - Fixed a bug that would prevent soul flamed arrows to catch normal fire in lava.
  - Improved logic and expanded API.

  ### [1.18.2-1.1.0.0] - 2022/09/24
  - Fixed compatibility with Malum (and probably some other mods).
  - Fixed a bug that would prevent soul flamed arrows to catch normal fire in lava.
  - Improved logic and expanded API.

  ### [1.16.5-1.1.0.0] - 2022/09/24
  - Fixed compatibility with Malum (and probably some other mods).
  - Fixed a bug that would prevent soul flamed arrows to catch normal fire in lava.
  - Improved logic and expanded API.
  - Dropped the `-final` because I can't bring myself to abandon a version if there's still someone using it.

  ### [1.19.2-1.0.0.2] - 2022/09/01
  - Fixed crash

  ### [1.19.1-1.0.0.2] - 2022/09/01
  - Fixed crash

  ### [1.19-1.0.0.2] - 2022/09/01
  - Fixed crash

  ### [1.18.2-1.0.0.2] - 2022/09/01
  - Fixed crash

  ### [1.16.5-1.0.0.2-final] - 2022/09/01
  - Fixed crash
  - I swear, *this* should be the FINAL version. 1.16.5 will not receive further updates.  
    (Unless I introduced new bugs with this update)

  ### [1.19.2-1.0.0.1] - 2022/08/31
  - Fixed modded Fire Aspect not cooking drops when oneshotting entities.
  - Fixed normal fire "flashing" when the flame goes out due to water.

  ### [1.19.1-1.0.0.1] - 2022/08/31
  - Fixed modded Fire Aspect not cooking drops when oneshotting entities.
  - Fixed normal fire "flashing" when the flame goes out due to water.

  ### [1.19-1.0.0.1] - 2022/08/31
  - Fixed modded Fire Aspect not cooking drops when oneshotting entities.
  - Fixed normal fire "flashing" when the flame goes out due to water.

  ### [1.18.2-1.0.0.1] - 2022/08/31
  - Fixed modded Fire Aspect not cooking drops when oneshotting entities.
  - Fixed normal fire "flashing" when the flame goes out due to water.

  ### [1.16.5-1.0.0.1-final] - 2022/08/31
  - Fixed modded Fire Aspect not cooking drops when oneshotting entities.
  - Fixed normal fire "flashing" when the flame goes out due to water.
  - I know, I said 1.16.5-1.0.0.0-final was the final version. Welp, I couldn't help but give it this last bug fix update.  
    So now *this* is the FINAL version. 1.16.5 will not receive further updates.

  ### [1.19.2-1.0.0.0] - 2022/08/30
  - New fire overlay for Soul Fire.
  - New flame rendering for entities burning from Soul Fire, along with increased damage.
  - Improved fire consistency.
  - Zombies on fire will transmit the kind of fire they're burning from.
  - Being set on fire will make entities burn from the correct fire type, furthermore new fire sources will change the fire type.
  - New enchantments: Soul Fire Aspect and Soul Flame.
  - API for modders to make modded fires behave consistently with little to no effort.

  ### [1.19.1-1.0.0.0] - 2022/08/30
  - New fire overlay for Soul Fire.
  - New flame rendering for entities burning from Soul Fire, along with increased damage.
  - Improved fire consistency.
  - Zombies on fire will transmit the kind of fire they're burning from.
  - Being set on fire will make entities burn from the correct fire type, furthermore new fire sources will change the fire type.
  - New enchantments: Soul Fire Aspect and Soul Flame.
  - API for modders to make modded fires behave consistently with little to no effort.

  ### [1.19-1.0.0.0] - 2022/08/30
  - New fire overlay for Soul Fire.
  - New flame rendering for entities burning from Soul Fire, along with increased damage.
  - Improved fire consistency.
  - Zombies on fire will transmit the kind of fire they're burning from.
  - Being set on fire will make entities burn from the correct fire type, furthermore new fire sources will change the fire type.
  - New enchantments: Soul Fire Aspect and Soul Flame.
  - API for modders to make modded fires behave consistently with little to no effort.

  ### [1.18.2-1.0.0.0] - 2022/08/30
  - New fire overlay for Soul Fire.
  - New flame rendering for entities burning from Soul Fire, along with increased damage.
  - Improved fire consistency.
  - Zombies on fire will transmit the kind of fire they're burning from.
  - Being set on fire will make entities burn from the correct fire type, furthermore new fire sources will change the fire type.
  - New enchantments: Soul Fire Aspect and Soul Flame.
  - API for modders to make modded fires behave consistently with little to no effort.

  ### [1.16.5-1.0.0.0-final] - 2022/08/30
  - New fire overlay for Soul Fire.
  - New flame rendering for entities burning from Soul Fire, along with increased damage.
  - Improved fire consistency.
  - Zombies on fire will transmit the kind of fire they're burning from.
  - Being set on fire will make entities burn from the correct fire type, furthermore new fire sources will change the fire type.
  - New enchantments: Soul Fire Aspect and Soul Flame.
  - API for modders to make modded fires behave consistently with little to no effort.
  - This is the FINAL version. 1.16.5 will not receive further updates.
</details>


[Unreleased]: https://github.com/crystal-nest/soul-fire-d

[v4.0.2]: https://github.com/crystal-nest/soul-fire-d/releases?q=4.0.2
[v4.0.1]: https://github.com/crystal-nest/soul-fire-d/releases?q=4.0.1
[v4.0.0]: https://github.com/crystal-nest/soul-fire-d/releases?q=4.0.0

[v3.4.3-beta]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.4.3-beta
[v3.4.2-beta]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.4.2-beta
[v3.4.1-beta]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.4.1-beta
[v3.4.0-beta]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.4.0-beta
[v3.3.5-alpha]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.3.5-alpha
[v3.3.4-alpha]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.3.4-alpha
[v3.3.3-alpha]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.3.3-alpha
[v3.3.2-alpha]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.3.2-alpha
[v3.3.1-alpha]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.3.1-alpha
[v3.3.0-alpha]: https://github.com/crystal-nest/soul-fire-d/releases?q=3.3.0-alpha

[1.20.4-3.2.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.20.4-3.2.1.0

[1.20.2-3.2.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.20.2-3.2.1.0
[1.20.2-3.2.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.20.2-3.2.0.1

[1.20.1-3.2.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.20.1-3.2.1.0
[1.20.1-3.2.0.1-final]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.20.1-3.2.0.1-final
[1.20.1-3.2.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.20.1-3.2.0.0

[1.19.4-3.2.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.4-3.2.1.0
[1.19.4-3.2.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.4-3.2.0.1
[1.19.4-3.2.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.4-3.2.0.0
[1.19.4-3.2.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.4-3.2.0.0
[1.19.4-3.1.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.4-3.1.0.0

[1.19.3-3.2.0.1-final]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.3-3.2.0.1-final
[1.19.3-3.2.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.3-3.2.0.0
[1.19.3-3.1.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.3-3.1.0.0
[1.19.3-3.0.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.3-3.0.1.0
[1.19.3-3.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.3-3.0.0.1
[1.19.3-3.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.3-3.0.0.0
[1.19.3-2.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.3-2.0.0.1
[1.19.3-2.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.3-2.0.0.0

[1.19.2-3.2.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-3.2.1.0
[1.19.2-3.2.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-3.2.0.1
[1.19.2-3.2.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-3.2.0.0
[1.19.2-3.1.0.0-final]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-3.1.0.0-final
[1.19.2-3.0.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-3.0.1.0
[1.19.2-3.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-3.0.0.1
[1.19.2-3.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-3.0.0.0
[1.19.2-2.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-2.0.0.1
[1.19.2-2.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-2.0.0.0
[1.19.2-1.1.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-1.1.0.0
[1.19.2-1.0.0.2]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-1.0.0.2
[1.19.2-1.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-1.0.0.1
[1.19.2-1.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.2-1.0.0.0

[1.19.1-1.1.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.1-1.1.0.0
[1.19.1-1.0.0.2]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.1-1.0.0.2
[1.19.1-1.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.1-1.0.0.1
[1.19.1-1.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19.1-1.0.0.0

[1.19-1.1.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19-1.1.0.0
[1.19-1.0.0.2]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19-1.0.0.2
[1.19-1.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19-1.0.0.1
[1.19-1.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.19-1.0.0.0

[1.18.2-3.2.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-3.2.1.0
[1.18.2-3.2.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-3.2.0.1
[1.18.2-3.2.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-3.2.0.0
[1.18.2-3.1.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-3.1.0.0
[1.18.2-3.0.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-3.0.1.0
[1.18.2-3.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-3.0.0.1
[1.18.2-3.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-3.0.0.0
[1.18.2-2.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-2.0.0.1
[1.18.2-2.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-2.0.0.0
[1.18.2-1.1.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-1.1.0.0
[1.18.2-1.0.0.2]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-1.0.0.2
[1.18.2-1.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-1.0.0.1
[1.18.2-1.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.18.2-1.0.0.0

[1.16.5-3.2.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-3.2.1.0
[1.16.5-3.2.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-3.2.0.1
[1.16.5-3.2.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-3.2.0.0
[1.16.5-3.1.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-3.1.0.0
[1.16.5-3.0.1.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-3.0.1.0
[1.16.5-3.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-3.0.0.1
[1.16.5-3.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-3.0.0.0
[1.16.5-2.0.0.1]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-2.0.0.1
[1.16.5-2.0.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-2.0.0.0
[1.16.5-1.1.0.0]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-1.1.0.0
[1.16.5-1.0.0.2-final]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-1.0.0.2-final
[1.16.5-1.0.0.1-final]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-1.0.0.1-final
[1.16.5-1.0.0.0-final]: https://github.com/crystal-nest/soul-fire-d/releases/tag/v1.16.5-1.0.0.0-final
