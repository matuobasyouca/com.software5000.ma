webpackJsonp([32],{159:function(t,s,e){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var a=e(2),i=e(6),o=e.n(i);s.default={components:{maHead:o.a},data:function(){return{isLoading:!1,formDatas:[],hasNextPage:!1,startPage:1,pageSize:10,revamp:!1,newProjectPage:!1,lvlName:"",totalConsume:"",discount:"",presonMsg:[]}},methods:{getFormDatas:function(t){var s=this;t?this.startPage++:this.startPage=1,this.$ajax(this.$joggle.business.selectMemberLvlList,{startPage:this.startPage,pageSize:this.pageSize},!0,function(e,a){if(a.close(),"ZS011000"==e.code){var i=t?s.formDatas.concat(e.data.list):e.data.list;i.forEach(function(t){s.$set(t,"_do",1==t.state)}),s.formDatas=i,s.hasNextPage=e.data.hasNextPage}else s.$message({type:"error",duration:1200,message:e.msg})})},remainMemberLv:function(){var t=this;this.$ajax(this.$joggle.business.updateMemberLvl,this.presonMsg,!0,function(s,e){e.close(),"ZS011000"===s.code?(t.newProjectPage=!1,t.$message({type:"success",message:s.msg,duration:1200})):(e.close(),t.$message({type:"error",message:s.msg}))})},newMemberLv:function(){var t=this,s={lvlName:this.lvlName,totalConsume:this.totalConsume.toString().split(".")[1]?parseFloat(this.totalConsume).toFixed(2):parseFloat(this.totalConsume),discount:this.discount.toString().split(".")[1]?parseFloat(this.discount).toFixed(1):parseFloat(this.discount)};this.$ajax(this.$joggle.business.insertMemberLvl,s,!0,function(s,e){"ZS011000"===s.code?(t.newProjectPage=!1,t.getFormDatas(!1),t.$message({type:"success",message:s.msg,duration:1200})):(e.close(),t.$message({type:"error",message:s.msg}))})},insertJudge:function(){var t=/^\d+(\.\d+)?$/,s="";if(e.i(a.c)(this.lvlName)?s="请输入等级名称":e.i(a.c)(this.totalConsume)?s="请输入累计消费金额":t.test(this.totalConsume)?e.i(a.c)(this.discount)?s="请输入会员折扣":(!t.test(this.discount)||parseFloat(this.discount)>10)&&(s="请输入0~10数值"):s="请输入正确的累计消费金额",""!=s)return this.$message({type:"error",duration:1e3,message:s}),!1;this.revamp?(this.presonMsg.lvlName=this.lvlName,this.presonMsg.totalConsume=this.totalConsume.toString().split(".")[1]?parseFloat(this.totalConsume).toFixed(2):parseFloat(this.totalConsume),this.presonMsg.discount=this.discount.toString().split(".")[1]?parseFloat(this.discount).toFixed(1):parseFloat(this.discount),this.remainMemberLv()):this.newMemberLv()},newProject:function(){this.newProjectPage=!0,this.lvlName="",this.totalConsume="",this.discount=""},editMsg:function(t){var s=this.formDatas[t];this.presonMsg=s,this.lvlName=s.lvlName,this.totalConsume=s.totalConsume,this.discount=s.discount,this.revamp=!0,this.newProjectPage=!0},switchBtn:function(t){var s=this.formDatas[t];s.state=1==s.state?2:1,this.presonMsg=s,this.remainMemberLv()},handleShowMore:function(){if(this.hasNextPage){var t=this.$loading();this.getFormDatas(!0,function(){t.close()})}},handleGoHome:function(){e.i(a.d)("/wap/merchant/home/manage/index.html")}},created:function(){this.getFormDatas(!1)}}},229:function(t,s,e){e(365);var a=e(0)(e(159),e(266),null,null);t.exports=a.exports},266:function(t,s){t.exports={render:function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{directives:[{name:"show",rawName:"v-show",value:!t.isLoading,expression:"!isLoading"}],staticClass:"level-page"},[e("ma-head",{on:{"home-click":t.handleGoHome}},[t._v("会员等级"),e("span",{staticClass:"new-project",on:{click:t.newProject}},[t._v("新建会员等级")])]),t._v(" "),e("div",{staticClass:"record-body"},[t.formDatas.length>0?e("ul",{staticClass:"record-ul"},[t._l(t.formDatas,function(s,a){return e("li",{key:a},[e("div",{staticClass:"row-one"},[e("div",{staticClass:"vip"},[e("zs-icon",{attrs:{icon:"member",size:"17",text:s.lvlName}})],1),t._v(" "),e("div",{staticClass:"discount"},[t._v(t._s(s.discount)+"折")]),t._v(" "),e("div",{staticClass:"cost"},[t._v("累积消费："+t._s(s.totalConsume))])]),t._v(" "),e("div",{staticClass:"row-two"},[e("zs-switch",{staticClass:"switch-btn",on:{change:function(s){t.switchBtn(a)}},model:{value:s._do,callback:function(t){s._do=t},expression:"item._do"}}),t._v(" "),s._do?e("span",{staticClass:"switch-h3"},[t._v("启用")]):e("span",{staticClass:"switch-h4"},[t._v("停用")]),t._v(" "),e("zs-icon",{staticClass:"make",attrs:{icon:"write",text:"编辑",size:"12"},on:{click:function(s){t.editMsg(a)}}})],1)])}),t._v(" "),e("div",{staticClass:"show_all_css",class:[{show_more_css:t.hasNextPage}],on:{click:t.handleShowMore}},[t._v(t._s(t.hasNextPage?"查看更多":"已显示全部")+"\n            ")])],2):e("div",[t._m(0)])]),t._v(" "),e("zs-slide-page",{staticClass:"ProjectPage",attrs:{title:1==t.revamp?"修改会员等级":"新建会员等级"},on:{"go-home":t.handleGoHome},model:{value:t.newProjectPage,callback:function(s){t.newProjectPage=s},expression:"newProjectPage"}},[e("div",{staticClass:"newProject-page"},[e("div",{staticClass:"new-body"},[e("ul",[e("li",[e("div",{staticClass:"li-title"},[e("em",[t._v("*")]),t._v(" 等级名称")]),t._v(" "),e("zs-input",{attrs:{maxlength:10,placeholder:"请输入会员等级名称"},model:{value:t.lvlName,callback:function(s){t.lvlName=s},expression:"lvlName"}})],1),t._v(" "),e("li",[e("div",{staticClass:"li-title"},[e("em",[t._v("*")]),t._v(" 累计消费")]),t._v(" "),e("zs-input",{attrs:{maxlength:25,placeholder:"请输入该等级累计消费金额"},model:{value:t.totalConsume,callback:function(s){t.totalConsume=s},expression:"totalConsume"}})],1),t._v(" "),e("li",[e("div",{staticClass:"li-title"},[e("em",[t._v("*")]),t._v(" 会员折扣")]),t._v(" "),e("zs-input",{attrs:{maxlength:25,placeholder:"0~10数值，可保留一位小数"},model:{value:t.discount,callback:function(s){t.discount=s},expression:"discount"}})],1)])]),t._v(" "),e("div",{staticClass:"new-bottom"},[e("zs-button",{staticClass:"btn-click",attrs:{type:"primary"},on:{click:t.insertJudge}},[t._v("确定")])],1)])])],1)},staticRenderFns:[function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("ul",{staticClass:"data-empty"},[e("li",[t._v("暂无会员等级信息")])])}]}},365:function(t,s){},447:function(t,s,e){t.exports=e(66)},66:function(t,s,e){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var a=e(1),i=e(229),o=e.n(i),n=e(5),l=(e.n(n),e(3),e(4));a.a.use(l.a),new a.a({el:"#app",render:function(t){return t(o.a)}})}},[447]);