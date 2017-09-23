# IndexAlphaView
Android Letter index custom View

Now many projects will basically use the right fast index column, of course, there are many online third party open source control can be used. But most of them are introduced into class libraries, and sometimes it's just for a little bit of functionality to introduce large capacity libraries,
It's really bad. So, according to other people's class library code changed, streamline the code, and found that this function can be achieved. Here's a look at the effect, and then do some simple explanation: 

![image](https://github.com/createBean/IndexAlphaView/blob/master/gif/record.gif)  
### Realization idea
The general process is as follows： 

- Gets the letters to display 

- Get the width and height values you need to use 

- Draw the circle background of letters and letters

- Dealing with View Touch events 

The idea is very simple to implement, the main difficulty or in the drawing coordinates, after all, I am not familiar with the custom View. 

### Text description  

**Get the width and height values you need to use**

When drawing the right column, we mainly use the following parameters to calculate the coordinates XY axis of the letters： 


- Total width of the control： mWidth 

- The height at which each letter can be assigned： mCellHeight 

- The distance between the first letter and the top of the control： mMarginTop (Used for central display) 

- The width of each letter itself, the central coordinates of the circle's background

When the width and height of the control changes, onSizeChanged is executed, which is also called during the first initialization, so we can get the first three parameters here.
This is considered the most will be 27 characters, if the first letter is numbers or special characters, we use "#" said.
MMarginTop might want to understand that the distance from the top of the control to the first letter is half of the total height of the control minus all letters. It should not be difficult to understand: 

**Draw letters** 


We can calculate the coordinates of each letter by the parameters obtained above.
Simply explain, the use of drawText drawing, using the X coordinates are the leftmost coordinates of the letter, and Y coordinates refers to the coordinates of the letter baseline. We can simply understand the coordinates of the lower left corner of the letter.


**x coordinate** 


To keep the letters centered in the control, the X coordinate is half of the total width of the control, minus half of the width of the letter：  

**y coordinate**

y The calculation of coordinates may be understood. First, like width, let the letters center up and down in the space they occupy, that is, half the height of the letters, plus half the height of the letters. Then add the height of all the letters in front of it and the distance from the top to the initials：
 
 
**Dealing with Touch events** 


The click and slide events of this control do the same thing by calculating the index of the currently pressed letter to get the letter, displaying the toast, and locating the list by the callback function. Just hide your toast when your fingers are lifted.

## Ending  


This control is relatively simple, and this is also a more suitable for entry control, may want to understand the place in the calculation.
Personally feel that if a control is not too complicated, it is best to achieve their own, or at least to see the source code. On the one hand, you can modify the control for their own use of the project, on the one hand, the emergence of bug, you can quickly locate, modify.
Properly.
