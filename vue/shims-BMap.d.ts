// declare module 'BMap' {
//     const BMap: any
//     export default BMap
//  }

export {};
declare global {
    interface Window {
        BMap: any;
    }
}
declare const BMapGL: any
// interface Window{ BMap: any; }