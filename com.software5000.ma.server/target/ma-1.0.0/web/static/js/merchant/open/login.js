webpackJsonp([24],{188:function(s,e,r){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var t=r(24),o=r(1),a=r(10);e.default={data:function(){return{isLoading:!0,form:{userName:"",password:"",userType:"merchant"},rules:{userName:[{required:!0,message:"请输入用户名",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}]},errorMsg:"",errorMsgArr:{userName:"请输入用户名",password:"请输入密码"}}},methods:{isDataEmpty:function(s){return""===this.form[s].trim()?(this.errorMsg=this.errorMsgArr[s],!1):(this.errorMsg="",!0)},isEmpty:function(){var s=this;for(var e in this.form)if(""===s.form[e].trim())return s.errorMsg=s.errorMsgArr[e],!1;return this.errorMsg="",!0},loginCheck:function(){if(!this.isEmpty())return!1;this.$loading(),this.login()},login:function(){var s=this,e={userId:this.form.userName,password:t.a.encode(this.form.password),userType:this.form.userType};this.$ajax(this.$joggle.merchant.open.login,JSON.stringify(e),!0,function(e,t){t.close(),"ZS001000"===e.code?(localStorage.businessId=e.data.businessId,localStorage.realName=e.data.realName,localStorage.userType=e.data.userType,localStorage.userName=e.data.userName,localStorage.businessName=e.data.businessName,setTimeout(function(){r.i(o.a)("/web/merchant/home/index.html")},200)):s.errorMsg=e.msg})}},created:function(){var s=this;this.form.userName=localStorage.userName||"",r.i(a.c)(function(){s.isLoading=!1})}}},282:function(s,e,r){function t(s){r(431)}var o=r(0)(r(188),r(365),t,null,null);s.exports=o.exports},365:function(s,e){s.exports={render:function(){var s=this,e=s.$createElement,r=s._self._c||e;return r("div",{directives:[{name:"show",rawName:"v-show",value:!s.isLoading,expression:"!isLoading"}],staticClass:"login-body"},[r("div",{staticClass:"login-img"}),s._v(" "),r("div",{staticClass:"login-content"},[r("span",{staticClass:"login-logo"}),s._v(" "),r("p",{staticClass:"login-title"},[s._v("中晟诚品-诚品车服")]),s._v(" "),r("div",{staticClass:"login-form"},[r("zs-form",{attrs:{model:s.form,rules:s.rules}},[r("p",{staticClass:"form-css user-css"},[r("span",{staticClass:"border"}),s._v(" "),r("zs-icon",{staticClass:"left-icon-css",attrs:{icon:"user",size:20}}),s._v(" "),r("zs-input",{attrs:{icon:"circle-cross",placeholder:"请输入账号"},on:{blur:function(e){s.isDataEmpty("userName")},keydown:function(e){if(!("button"in e)&&s._k(e.keyCode,"enter",13))return null;s.loginCheck(e)}},model:{value:s.form.userName,callback:function(e){s.form.userName=e},expression:"form.userName"}})],1),s._v(" "),r("p",{staticClass:"form-css"},[r("span",{staticClass:"border"}),s._v(" "),r("zs-icon",{staticClass:"left-icon-css",attrs:{icon:"pwd",size:20}}),s._v(" "),r("zs-input",{attrs:{icon:"circle-cross",placeholder:"请输入密码",type:"password"},on:{blur:function(e){s.isDataEmpty("password")},keydown:function(e){if(!("button"in e)&&s._k(e.keyCode,"enter",13))return null;s.loginCheck(e)}},model:{value:s.form.password,callback:function(e){s.form.password=e},expression:"form.password"}})],1),s._v(" "),r("div",{directives:[{name:"show",rawName:"v-show",value:s.errorMsg.length>0,expression:"errorMsg.length>0"}],staticClass:"error-div"},[r("zs-icon",{staticClass:"danger-icon-css",attrs:{icon:"danger",size:14}}),s._v(" "),r("span",{staticClass:"error-message"},[s._v(s._s(s.errorMsg))])],1),s._v(" "),r("zs-button",{staticClass:"form-btn",attrs:{type:"primary"},on:{click:s.loginCheck}},[s._v("登录")])],1)],1)])])},staticRenderFns:[]}},431:function(s,e){},475:function(s,e,r){s.exports=r(65)},65:function(s,e,r){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var t=r(2),o=r(282),a=r.n(o),i=r(5),n=(r.n(i),r(3),r(4));t.a.use(n.a),new t.a({el:"#app",render:function(s){return s(a.a)}})}},[475]);