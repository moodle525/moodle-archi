        function a(i){
	a(i);
}
   function a(i){
	   switch(i){
            case 1:
       document.getElementById("js_ta1").style.display="block";
       document.getElementById("js_ta2").style.display="none";
	   document.getElementById("js_ta3").style.display="none";
       break;
        case 2:
       document.getElementById("js_ta1").style.display="none";
       document.getElementById("js_ta2").style.display="block";
	   document.getElementById("js_ta3").style.display="none";
      break;
	    case 3:
       document.getElementById("js_ta1").style.display="none";
       document.getElementById("js_ta2").style.display="none";
	   document.getElementById("js_ta3").style.display="block";
      break;
 }
}

