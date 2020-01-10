---
title: Ionic Battery Alert 
description: Device's battery status notification
---

## Supported Platforms
- Android
- Ios

### How to Use
```
declare var BatteryInfo: any; //paste it below the import section

Battery listener will indicate the below 10% of battery level

//button onclick function 
checkBatteryStatus() { 
// arg = 0 (Alert) , 1 (Toast)
BatteryInfo.coolMethod(arg, (response) => {
      console.log(response);
    }, (error => {
      console.log(error);
})
}
```
