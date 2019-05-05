<template>
    <tbody :class="[prefixCls + '__body']">
    <tr
            v-for="(row,index) in rebuildData"
            :class="rowClasses(row._index)"
            :key="row._rowKey"
            @mouseenter.stop="handleMouseIn(row._index)"
            @mouseleave.stop="handleMouseOut(row._index)"
            @click.stop="clickCurrentRow(row._index)">
        <td
                v-for="(col,index) in columns"
                :class="alignCls(col,row)"
                :key="col._columnKey">
            <cell
                    :prefix-cls="prefixCls"
                    :row="row"
                    :column="col"
                    :natural-index="index"
                    :index="row._index"
                    :checked="rowChecked(row._index)"
                    :disabled="rowDisabled(row._index)"
            ></cell>
        </td>
    </tr>
    </tbody>
</template>
<script>
    import Mixin from './mixin';
    import Cell from './cell';
    export default {
        name: 'TableBody',
        mixins: [Mixin],
        components: {
            Cell
        },
        props: {
            prefixCls: String,
            columns: Array,
            data: Array,
            rebuildData: Array,
            rowClassName: Function
        },
        methods: {
            /**
             * 为特定row设定class
             * @param _index 需要设置className的row的_index属性
             * @param this.rowClassName 由父级传入的定义row className的方法
             * @returns
             */
            rowClsName(_index){
                return this.rowClassName(this.data[_index], _index);
            },

            /**
             * 设置row的所有class
             * @param _index
             */
            rowClasses(_index){
                return [
                    `${this.prefixCls}__row`,
                    this.rowClsName(_index),
                    {
                        [`${this.prefixCls}__row-highlight`]: this.data[_index] && this.data[_index]._isHighlight,
                        [`${this.prefixCls}__row-hover`]: this.data[_index] && this.data[_index]._isHover
                    }
                ]
            },
            handleMouseIn (_index) {
                this.$emit('handleMouseIn', _index);
            },
            handleMouseOut (_index) {
                this.$emit('handleMouseOut', _index);
            },
            clickCurrentRow (_index) {
                this.$emit('clickCurrentRow', _index);
            },
            rowChecked (_index) {
                return this.data[_index] && this.data[_index]._isChecked;
            },
            rowDisabled(_index){
                return this.data[_index] && this.data[_index]._isDisabled;
            }
        }
    }
</script>
