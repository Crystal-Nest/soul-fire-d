![Soul Fire'd banner](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/soul-fire-d/banner.gif)

---
![Minecraft](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/minecraft/minecraft.svg)[![1.20.4](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/minecraft/1-20-4.svg)](https://modrinth.com/mod/soul-fire-d/versions?g=1.20.4)![Separator](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/separator.svg)[![1.20.2](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/minecraft/1-20-2.svg)](https://modrinth.com/mod/soul-fire-d/versions?g=1.20.2)![Separator](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/separator.svg)[![1.20.1](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/minecraft/1-20-1.svg)](https://modrinth.com/mod/soul-fire-d/versions?g=1.20.1)![Separator](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/separator.svg)[![1.19.4](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/minecraft/1-19-4.svg)](https://modrinth.com/mod/soul-fire-d/versions?g=1.19.4)![Separator](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/separator.svg)[![1.19.2](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/minecraft/1-19-2.svg)](https://modrinth.com/mod/soul-fire-d/versions?g=1.19.2)![Separator](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/separator.svg)[![1.18.2](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/minecraft/1-18-2.svg)](https://modrinth.com/mod/soul-fire-d/versions?g=1.18.2)

![Loader](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/loader/loader.svg)[![NeoForge](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/loader/neoforge.svg)](https://modrinth.com/mod/soul-fire-d/versions?l=neoforge)![Separator](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/separator.svg)[![Forge](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/loader/forge.svg)](https://modrinth.com/mod/soul-fire-d/versions?l=forge)![Separator](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/separator.svg)[![Fabric](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/loader/fabric.svg)](https://modrinth.com/mod/soul-fire-d/versions?l=fabric)

![Overlay](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/side/client-server.svg)

![Issues](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/github/issues.svg)[![GitHub](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/github/github.svg)](https://github.com/crystal-nest/soul-fire-d/issues)

---

## **Description**

In Minecraft there's Soul Fire, yet if you or a mob catch fire because of it nothing will change from normal fire.  
This mod makes Soul Fire actually work as one would expect, all whilst providing an *easy-to-use* API for modders that want to add their own custom fire and have it behave consistently.

## **Features**

- **Soul Fire overlay**  
  ![Overlay](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/soul-fire-d/overlay.gif)
- **Entities catching fire from Soul Fire will burn with actual Soul Fire.**  
  Burning from Soul Fire will deal 2 damage per second.  
  ![Sheep](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/soul-fire-d/sheep.gif)
- **Actually consistent Soul Fire behavior.**  
  For instance, but not limited to, zombies burning from Soul Fire and arrows passing through Soul Fire can set their targets on Soul Fire.  
  ![Arrows](https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/soul-fire-d/arrow.gif)
- **Two new enchantments: Soul Fire Aspect and Soul Flame.**  
  As their name suggests, they work exactly the same as Fire Aspect and Flame with the only difference being the kind of fire the target will burn from, with the consequent damage increase.  
  Both these enchantments can be highly configured individually, enabling (default) or disabling them and more.
- **API for custom fire types:**  
  For mod/datapack creators who want to add their own custom fire(s), or implement another mod's fire, this mod provides an *easy-to-use* API to register your fire(s) and have it(them) behave consistently **automatically**.  
  Furthermore for each new fire registered using this API there can be new custom Fire Aspect and Flame enchantments available in game **automatically**.  
  See the bottom section *For developers* to know more.

## **Compatibilities**

A list of mods adding full compatibility with Soul Fire'd by integrating Soul Fire behavior and, in some cases, registering new Fires:

| Mod                                                                                                          | Loader |
|:-------------------------------------------------------------------------------------------------------------|:------:|
| [Oh The Biomes You'll Go](https://modrinth.com/mod/biomesyougo)                                              |     Forge      |
| [Tetra](https://modrinth.com/mod/decorative-blocks) with [Tetracelium](https://modrinth.com/mod/tetracelium) | Forge/NeoForge |
| [Decorative Blocks](https://modrinth.com/mod/decorative-blocks)                                              |      All       |
| [Danger Close](https://modrinth.com/mod/danger-close)                                                        |      All       |
| [Torch hit!](https://modrinth.com/mod/torch-hit)                                                             |      All       |

If you want your mod to appear in this list, open an issue [here](https://github.com/Crystal-Nest/soul-fire-d/issues/new?assignees=Crystal-Spider&labels=question%2Cmedium+priority&projects=&template=information_request.yml) and provide a link to your mod!

## **Dependencies**

| Mod                                                                     |         Loader         | Requirement |
|:------------------------------------------------------------------------|:----------------------:|:-----------:|
| [Cobweb](https://modrinth.com/mod/forge-config-api-port)                |          All           |  Required   |
| [Forge Config API Port](https://modrinth.com/mod/forge-config-api-port) | Fabric; Forge ≥ 1.20.2 |  Required   |

## **License and right of use**

Feel free to use this mod for any modpack or video, just be sure to give credit and possibly link [here](https://github.com/crystal-nest/soul-fire-d#readme).  
This project is published under the [GNU General Public License v3.0](https://github.com/crystal-nest/soul-fire-d/blob/master/LICENSE).

## **For developers**

To learn how to use the provided API follow the [Wiki](https://github.com/crystal-nest/soul-fire-d/wiki) on [GitHub](https://github.com/crystal-nest/soul-fire-d).  
The API is available with datapacks too, to learn more follow the [dedicated Wiki page](https://github.com/crystal-nest/soul-fire-d/wiki/Data-Driven-Fires).

## **Support us**

<a href="https://crystalnest.it"><img alt="Crystal Nest Website" src="https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/crystal-nest/pic512.png" width="14.286%"></a><a href="https://discord.gg/BP6EdBfAmt"><img alt="Discord" src="https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/discord/discord512.png" width="14.286%"></a><a href="https://www.patreon.com/crystalspider"><img alt="Patreon" src="https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/patreon/patreon512.png" width="14.286%"></a><a href="https://ko-fi.com/crystalspider"><img alt="Ko-fi" src="https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/kofi/kofi512.png" width="14.286%"></a><a href="https://github.com/Crystal-Nest"><img alt="Our other projects" src="https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/github/github512.png" width="14.286%"><a href="https://modrinth.com/organization/crystal-nest"><img alt="Modrinth" src="https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/modrinth/modrinth512.png" width="14.286%"></a><a href="https://www.curseforge.com/members/crystalspider/projects"><img alt="CurseForge" src="https://raw.githubusercontent.com/crystal-nest/mod-fancy-assets/main/curseforge/curseforge512.png" width="14.286%"></a>

[![Bisect Hosting](https://www.bisecthosting.com/partners/custom-banners/d559b544-474c-4109-b861-1b2e6ca6026a.webp "Bisect Hosting")](https://bisecthosting.com/crystalspider)
