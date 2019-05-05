/**
 * Created by JANZ on 2017/3/19.
 */
export default{
    methods : {
        /**
         * 设置列样式和单元格样式
         * @param column 当前列
         * @param row 当前行
         * @returns
         */
        alignCls (column, row = {}) {
            column.align = column.align || 'center';
            let cellClassName = '';
            if (row.cellClassName && column.key && row.cellClassName[column.key]) {
                cellClassName = row.cellClassName[column.key];
            }
            return [
                {
                    [`${cellClassName}`] : cellClassName,
                    [`${column.className}`] : column.className,
                    [`${this.prefixCls}-column__${column.align}`] : column.align,
                }
            ];
        }
    }
}