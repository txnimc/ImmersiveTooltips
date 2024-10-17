---
outline: deep
---

# Message Presets

Immersive Messages includes a few pre-configured message preset helper functions for common applications. 

These give you an `ImmersiveMessage` object, which you will need to send manually, but you can of course 
configure it with custom settings before doing so if you would like to change the default presets.

# Toasts

![img.png](public/assets/toast.png)

These will show on the left hand side of the screen, in the top corner, like an achievement, useful for
reminder messages without being too intrusive. You can get an instance with:

```java
ImmersiveMessage.toast(float duration, String title, String subtitle);
```


# Popup Messages

![img.png](public/assets/popup.png)

These will pop up right above the hotbar, a bit more intrusive, but with the intention of providing a direct message 
to the player that's hard to ignore. You can get an instance with:
```java
ImmersiveMessage.popup(float duration, String title, String subtitle);
```
