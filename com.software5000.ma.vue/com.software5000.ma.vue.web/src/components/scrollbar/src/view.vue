<script>
    export default {
        name : 'scrollview',
        props : {
            viewClass : {},
            viewStyle : {},
            tag : {
                type : String,
                default : 'div'
            }
        },
        render(h){
            return h(this.tag, {
                class : ['zs-scrollbar__view', this.viewClass],
                style : this.viewStyle,
                ref : 'resize'
            }, this.$slots.default);
        }
    };
</script>
