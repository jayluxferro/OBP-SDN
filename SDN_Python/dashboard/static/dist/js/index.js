$(function(){
  // setting current year
  $('#currentYear').html(new Date().getFullYear())


  function getNodeInventory(){
    $.get('/nodes', null, data => {
      if(data && data.nodes && data.nodes.node !== undefined && data.nodes.node.length){
        let recvData = data.nodes.node
        // switches
        $('#switches').html(recvData.length)

        // controller IP
        $('#ip').html(recvData[0]['flow-node-inventory:ip-address'])

        // version
        $('#version').html(recvData[0]['flow-node-inventory:software'])

        // number of nodes
        let nodes = 0

        for(let i = 0; i < recvData.length; i++){
          let node_connector = recvData[i]['node-connector']
          for(let j = 0; j < node_connector.length; j++){
            let node_connector2 = node_connector[j]
            
            if(node_connector2['address-tracker:addresses'] !== undefined && node_connector2['address-tracker:addresses'].length){
              nodes += node_connector2['address-tracker:addresses'].length
            }

          }
        }   
        $('#nodes').html(nodes)
  
      }
    })
  }


  let data = Array.apply(null, Array(100)).map(Number.prototype.valueOf,0)
  let max = 30000
  function getTraffic(){

    $.get('/nodes', null, d => {
      if(d && d.nodes && d.nodes.node !== undefined && d.nodes.node.length){
          let recvData = d.nodes.node
          // just display one data
          if(recvData.length >= 1){
            let tx = recvData[0]['node-connector'][0]['opendaylight-port-statistics:flow-capable-node-connector-statistics']['bytes']['transmitted']
            // console.log(tx)
            max = tx * 2
            data.push(tx)
          }else{
            data.push(0)
          } 
        }else{
          data.push(0)
        }
    })

    if(data.length > 100)
      data = data.slice(1)      
   

    // zip the generated y values with the x values
    let res = []
    for(let i = 0; i < data.length; i++){
      res.push([i, data[i]])
    }
    return res
  }  


  let interactive_plot = $.plot('#interactive', [getTraffic()], {
      grid  : {
        borderColor: '#f3f3f3',
        borderWidth: 1,
        tickColor  : '#f3f3f3'
      },
      series: {
        shadowSize: 0, // Drawing is faster without shadows
        color     : '#3c8dbc'
      },
      lines : {
        fill : true, //Converts the line chart to area chart
        color: '#3c8dbc'
      },
      yaxis : {
        min : 0,
        max : max,
        show: true
      },
      xaxis : {
        show: true
      }
  })

  function updatePlot(){
    interactive_plot.setData([getTraffic()])

    interactive_plot.draw()
  }


  //update view
  setInterval(function(){
    updatePlot()
    getNodeInventory()
  }, 1000)
})
