<template>
    <div :class="classes" ref="cell">
        <template v-if="renderType === 'index'">{{naturalIndex + 1}}</template>
        <template v-if="renderType === 'selection'">
            <zs-checkbox :value="checked" @change="toggleSelect" :disabled="disabled"></zs-checkbox>
        </template>
        <template v-if="renderType === 'normal'"><span>{{ row[column.key] }}</span></template>
    </div>
</template>
<script>
    import Vue from 'vue/dist/vue.js'
    import ZsCheckbox from '../../checkbox';
    export default {
        name : 'Cell',
        components : {
            ZsCheckbox
        },
        props : {
            prefixCls : String,
            column : Object,
            row : Object,
            naturalIndex : Number,
            index : Number,
            disabled : Boolean,
            checked : Boolean
        },
        data(){
            return {
                renderType : '',
                context : this.$parent.$parent.currentContext
            }
        },
        computed : {
            classes(){
                return [
                    `${this.prefixCls}__cell`,
                    {
                        [`${this.prefixCls}__cell-ellipsis`] : this.column.ellipsis || false
                    }
                ];
            }
        },
        methods : {
            compile () {
                if (this.column.render) {
                    const $parent = this.context;
                    const template = this.column.render(this.row, this.column, this.index,$parent);
                    const cell = document.createElement('div');
                    cell.innerHTML = template;
                    this.$el.innerHTML = '';
                    let methods = {};
                    Object.keys($parent).forEach(key => {
                        const func = $parent[key];
                        if (typeof(func) === 'function' && (func.name  === 'boundFn' || func.name === 'n')) {
                            methods[key] = func;
                        }
                    });
                    const res = Vue.compile(cell.outerHTML);
                    const component = new Vue({
                        render: res.render,
                        staticRenderFns: res.staticRenderFns,
                        methods: methods,
                        data () {
                            return $parent._data;
                        }
                    });
                    const Cell = component.$mount();
                    this.$refs.cell.appendChild(Cell.$el);
                }
            },
            toggleSelect(){
                this.$parent.$parent.toggleSelect(this.index);
            }
        },
        created () {
            if (this.column.type === 'index') {
                this.renderType = 'index';
            } else if (this.column.type === 'selection') {
                this.renderType = 'selection';
            } else if (this.column.render) {
                this.renderType = 'render';
            } else {
                this.renderType = 'normal';
            }
        },
        mounted () {
            this.$nextTick(() => {
                this.compile();
            });
        },
        watch: {
            naturalIndex () {
//                this.destroy();
                this.compile();
            }
        }
    }
</script>
