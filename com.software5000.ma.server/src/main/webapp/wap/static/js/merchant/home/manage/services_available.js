webpackJsonp([30],{168:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=s(2),a=s(6),o=s.n(a);t.default={components:{maHead:o.a},data:function(){return{isLoading:!1,searchTable:{orderBy:"topState desc,sort asc,updateTime desc",startPage:1,pageSize:10,searchValue:"",itemType:""},datas:[],hasNextPage:!1,showDeleteDialog:!1,id:"",newProject:{itemName:"",salePrice:"",originalPrice:"",itemType:"",sort:1,topState:0},ServiceItemType:[],newProjectPage:!1,revamp:!1,getIndex:0}},methods:{getDates:function(e){var t=this;e?this.searchTable.startPage++:this.searchTable.startPage=1,this.$ajax(this.$joggle.merchant.setting.selectPageServiceItem,this.searchTable,!0,function(s,i){if(i.close(),"ZS011000"==s.code){document.body.scrollTop=0,t.hasNextPage=s.data.hasNextPage;var a=e?t.datas.concat(s.data.list):s.data.list;a.forEach(function(e){t.$set(e,"_top",1==e.topState)}),t.datas=a}else t.$message({type:"error",duration:1200,message:s.msg})})},updateMessage:function(e){var t=this,s=e;s.salePrice&&(s.salePrice=s.salePrice.toString().split(".")[1]?parseFloat(s.salePrice).toFixed(2):parseFloat(s.salePrice)),this.$ajax(this.$joggle.merchant.setting.updateServiceItem,s,!0,function(e,i){i.close(),"ZS011000"==e.code?(t.newProjectPage=!1,t.$message({type:"success",duration:1200,message:t.revamp?"修改成功":1==s.topState?"置顶成功":"取消置顶成功"})):t.$message({type:"error",duration:1200,message:t.revamp?e.msg:1==s.topState?"置顶失败":"取消置顶失败"})})},deleteItem:function(e){var t=this;e?this.$ajax(this.$joggle.merchant.setting.deleteServiceItem,{id:this.id},!0,function(e,s){s.close(),"ZS011000"==e.code?(t.showDeleteDialog=!1,t.getDates(),t.$message({type:"success",duration:1200,message:"删除成功"})):t.$message({type:"error",duration:1200,message:e.msg})}):(this.showDeleteDialog=!1,this.$message({type:"error",duration:1200,message:"服务项正在被使用，无法删除"}))},deleteMsg:function(){var e=this;this.$ajax(this.$joggle.merchant.setting.selectWhetherServiceItemEdit,{id:this.id},!0,function(t,s){s.close(),"ZS011000"==t.code?e.deleteItem(t.data):e.$message({type:"error",duration:1200,message:t.msg})})},insertProject:function(){var e=this;this.newProject.salePrice=parseFloat(this.newProject.salePrice).toFixed(2),this.newProject.originalPrice=this.newProject.salePrice,this.$ajax(this.$joggle.merchant.setting.insertServiceItem,this.newProject,!0,function(t,s){s.close(),"ZS011000"==t.code?(e.$message({type:"success",duration:1200,message:"新建成功"}),e.newProjectPage=!1,e.getDates()):e.$message({type:"error",duration:1200,message:t.msg})})},insertJudge:function(){var e=/^\d+(\.\d+)?$/,t="";if(s.i(i.c)(this.newProject.itemName)?t="请输入项目名称":s.i(i.c)(this.newProject.salePrice)?t="请输入项目价格":e.test(this.newProject.salePrice)?s.i(i.c)(this.newProject.itemType)&&(t="请选择项目所属分类"):t="请输入正确的价格",""!=t)return this.$message({type:"error",duration:1e3,message:t}),!1;if(this.newProject.originalPrice=this.newProject.salePrice,this.revamp){var a={};a=Object.assign({},this.newProject),a.id=this.id,this.$set(this.datas[this.getIndex],"itemName",this.newProject.itemName),this.$set(this.datas[this.getIndex],"salePrice",this.newProject.salePrice.toString().split(".")[1]?parseFloat(this.newProject.salePrice).toFixed(2):parseFloat(this.newProject.salePrice)),this.$set(this.datas[this.getIndex],"originalPrice",this.newProject.salePrice.toString().split(".")[1]?parseFloat(this.newProject.salePrice).toFixed(2):parseFloat(this.newProject.salePrice)),this.$set(this.datas[this.getIndex],"itemType",this.newProject.itemType),this.$set(this.datas[this.getIndex],"itemTypeDes",this.ServiceItemType[this.newProject.itemType-1][1]),this.$set(this.datas[this.getIndex],"sort",this.newProject.sort),this.$set(this.datas[this.getIndex],"topState",this.newProject.topState),this.updateMessage(a)}else this.insertProject()},projectClick:function(){this.newProject.itemName="",this.newProject.salePrice="",this.newProject.itemType="",this.revamp=!1,this.newProjectPage=!0},deleteBtn:function(e){var t=this.datas[e];this.id=t.id,this.showDeleteDialog=!0},setAgain:function(e){var t=this.datas[e];this.getIndex=e,this.newProject.itemName=t.itemName,this.newProject.salePrice=t.salePrice,this.newProject.originalPrice=t.originalPrice,this.newProject.itemType=t.itemType+"",this.newProject.sort=t.sort,this.newProject.topState=t.topState,this.id=t.id,this.revamp=!0,this.newProjectPage=!0},isTop:function(e){var t=this.datas[e],s={};s.itemName=t.itemName,s.salePrice=t.salePrice,s.originalPrice=t.originalPrice,s.itemType=t.itemType,s.sort=t.sort,s.id=t.id,s.topState=0==t.topState?1:0,this.datas[e].topState=0==t.topState?1:0,this.updateMessage(s)},handleShowMore:function(){this.hasNextPage&&this.getDates(!0)},handleGoHome:function(){s.i(i.d)("/wap/merchant/home/manage/index.html")}},created:function(){var e=this;this.getDates();var t={codeTypes:"ServiceItemType"};this.$ajax(this.$joggle.mzscp.selectConstantTypes,JSON.stringify(t),!1,function(t){if("ZS011000"==t.code){var s=t.data;e.ServiceItemType=s.ServiceItemType}else e.$message({type:"error",duration:1200,message:t.msg})})}}},243:function(e,t,s){s(360);var i=s(0)(s(168),s(285),null,null);e.exports=i.exports},285:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{directives:[{name:"show",rawName:"v-show",value:!e.isLoading,expression:"!isLoading"}],staticClass:"services-page"},[s("ma-head",{on:{"home-click":e.handleGoHome}},[e._v("服务项目"),s("span",{staticClass:"new-project",on:{click:e.projectClick}},[e._v("新建项目")])]),e._v(" "),s("div",{staticClass:"services-body"},[e.datas.length>0?s("ul",{staticClass:"services-ul"},[e._l(e.datas,function(t,i){return s("li",{key:i},[s("div",{staticClass:"row-top"},[s("div",{staticClass:"sort"},[e._v(e._s(t.itemTypeDes)+"类")]),e._v(" "),s("div",{staticClass:"mid"},[s("p",[e._v(e._s(t.itemName))]),e._v(" "),s("em",[e._v("￥"+e._s(t.salePrice))])]),e._v(" "),s("zs-switch",{on:{change:function(t){e.isTop(i)}},model:{value:t._top,callback:function(e){t._top=e},expression:"item._top"}}),e._v(" "),s("span",{staticClass:"top",class:[{curr:t._top}]},[e._v("置顶")])],1),e._v(" "),s("div",{staticClass:"row-bottom"},[s("zs-icon",{attrs:{icon:"write",text:"编辑"},on:{click:function(t){e.setAgain(i)}}}),e._v(" "),s("zs-icon",{attrs:{icon:"delete3",text:"删除"},on:{click:function(t){e.deleteBtn(i)}}})],1)])}),e._v(" "),s("div",{staticClass:"show_all_css",class:[{show_more_css:e.hasNextPage}],on:{click:e.handleShowMore}},[e._v(e._s(e.hasNextPage?"查看更多":"已显示全部")+"\n            ")])],2):s("div",[e._m(0)])]),e._v(" "),s("zs-dialog",{staticClass:"delete-dialog",attrs:{"show-close":""},model:{value:e.showDeleteDialog,callback:function(t){e.showDeleteDialog=t},expression:"showDeleteDialog"}},[s("div",{staticClass:"dialog-body"},[s("div",{staticClass:"money-img"}),e._v(" "),s("p",{staticClass:"h3"},[e._v("是否删除该项目？")])]),e._v(" "),s("template",{slot:"footer"},[s("zs-button",{attrs:{type:"cancel"},on:{click:function(t){e.showDeleteDialog=!1}}},[e._v("取消\n            ")]),e._v(" "),s("zs-button",{attrs:{type:"primary"},on:{click:e.deleteMsg}},[e._v("确定\n            ")])],1)],2),e._v(" "),s("zs-slide-page",{staticClass:"ProjectPage",attrs:{title:1==e.revamp?"修改项目":"新建项目"},on:{"go-home":e.handleGoHome},model:{value:e.newProjectPage,callback:function(t){e.newProjectPage=t},expression:"newProjectPage"}},[s("div",{staticClass:"newProject-page"},[s("div",{staticClass:"new-body"},[s("ul",[s("li",[s("div",{staticClass:"li-title"},[s("em",[e._v("*")]),e._v(" 项目名称")]),e._v(" "),s("zs-input",{attrs:{maxlength:10,placeholder:"请输入项目名称"},model:{value:e.newProject.itemName,callback:function(t){e.newProject.itemName=t},expression:"newProject.itemName"}}),e._v(" "),s("span",{staticClass:"li-length"},[e._v(e._s(e.newProject.itemName.length)+"/10")])],1),e._v(" "),s("li",[s("div",{staticClass:"li-title"},[s("em",[e._v("*")]),e._v(" 价格")]),e._v(" "),s("zs-input",{attrs:{maxlength:25,placeholder:"请输入项目价格"},model:{value:e.newProject.salePrice,callback:function(t){e.newProject.salePrice=t},expression:"newProject.salePrice"}})],1),e._v(" "),s("li",[s("div",{staticClass:"li-title"},[s("em",[e._v("*")]),e._v(" 所属分类")]),e._v(" "),s("select",{directives:[{name:"model",rawName:"v-model",value:e.newProject.itemType,expression:"newProject.itemType"}],staticClass:"li-select",attrs:{placeholder:"请选择所属分类"},on:{change:function(t){var s=Array.prototype.filter.call(t.target.options,function(e){return e.selected}).map(function(e){return"_value"in e?e._value:e.value});e.newProject.itemType=t.target.multiple?s:s[0]}}},[s("option",{attrs:{value:""}},[e._v("请选择所属分类")]),e._v(" "),e._l(e.ServiceItemType,function(t,i,a){return s("option",{key:a,domProps:{value:t[0]}},[e._v("\n                                "+e._s(t[1])+"类\n                            ")])})],2),e._v(" "),s("zs-icon",{staticClass:"li-deal",attrs:{icon:"arrow-right",size:"10"}})],1)])]),e._v(" "),s("div",{staticClass:"new-bottom"},[s("zs-button",{staticClass:"btn-click",attrs:{type:"primary"},on:{click:e.insertJudge}},[e._v("确定")])],1)])])],1)},staticRenderFns:[function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("ul",{staticClass:"data-empty"},[s("li",[e._v("暂无服务项目信息")])])}]}},360:function(e,t){},474:function(e,t,s){e.exports=s(71)},71:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=s(1),a=s(243),o=s.n(a),r=s(5),c=(s.n(r),s(3),s(4));i.a.use(c.a),new i.a({el:"#app",render:function(e){return e(o.a)}})}},[474]);