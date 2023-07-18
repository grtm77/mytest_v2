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

// interface Window{ BMap: any; }