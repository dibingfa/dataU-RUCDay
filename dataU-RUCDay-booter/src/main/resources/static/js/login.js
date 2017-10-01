window.onload=function(){
    var aInput=document.getElementsByTagName('input');
    var oUser=aInput[0];
    var oPwd=aInput[1]
    var aI=document.getElementsByTagName('i')[0];
    
    
    
    //用户名检测
    
    oUser.onfocus=function(){
        aI.innerHTML='';
        oUser.removeAttribute("placeholder");
    }
    
    oUser.onkeyup=function(){
        
    }
    
    oUser.onblur=function(){
        var tel = /1[3|4|5|7|8][0-9]\d{8}$/;
        if(!tel.test(this.value)){
            aI.innerHTML='手机号不正确';
        }else if(this.value==""){
            aI.innerHTML='手机号不可为空';
        }
    }
    
    //密码检测
    
    oPwd.onfocus=function(){
        if(oUser.value==""){
            aI.innerHTML='手机号不可为空';
        }
        oPwd.removeAttribute("placeholder");
    }
    oPwd.onblur=function(){
        if(this.value==""){
            aI.innerHTML='密码不可为空';
        }
        oPwd.setAttribute("placeholder");
        oPwd.style.placeholder='请输入确认密码';
    }
    
    
}