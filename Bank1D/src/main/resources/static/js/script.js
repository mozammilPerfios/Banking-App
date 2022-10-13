$(document).ready(function()
{
var firstForm=document.getElementsByClassName("form-step1");
var secondForm=document.getElementsByClassName("form-step2");
var thirdForm=document.getElementsByClassName("form-step3");
var progress=document.getElementById("progress");
var next1=document.getElementById("next1");
next1.onclick=function(){
    console.log("hello");
    for (i = 0, len = firstForm.length; i < len; i++) {
        firstForm[i].style.left = '-550px';
      }
    for (i = 0, len = secondForm.length; i < len; i++) {
        secondForm[i].style.left = '0px';
      }
    progress.style.width="300px";
}


var next2=document.getElementById("next2");
next2.onclick=function(){
    for (i = 0, len = secondForm.length; i < len; i++) {
        secondForm[i].style.left = '-550px';
      }
    console.log("hello");
    for (i = 0, len = thirdForm.length; i < len; i++) {
        thirdForm[i].style.left = '0px';
      }
      progress.style.width="480px";
    }
var back1=document.getElementById("back1");
back1.onclick=function(){
    console.log("hello");
    for (i = 0, len = firstForm.length; i < len; i++) {
        firstForm[i].style.left = '0px';
      }
    for (i = 0, len = secondForm.length; i < len; i++) {
        secondForm[i].style.left = '550px';
      }
      progress.style.width="140px"; 
}



var back2=document.getElementById("back2");
back2.onclick=function(){
    for (i = 0, len = secondForm.length; i < len; i++) {
        secondForm[i].style.left = '0px';
      }
    console.log("hello");
    for (i = 0, len = thirdForm.length; i < len; i++) {
        thirdForm[i].style.left = '550px';
      }
      progress.style.width="280px";

}


$('#city').change(function(){
var e=document.getElementById("city");
console.log(e.value);
var d=$("#branchI").val(e.value);	
cosole.log()
});
});





