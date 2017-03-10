/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.profitbricks.rest.domain.DataCenter;
import com.profitbricks.rest.domain.Location;
import com.profitbricks.rest.domain.Nic;
import com.profitbricks.rest.domain.PBObject;
import com.profitbricks.rest.domain.Server;
import com.profitbricks.rest.domain.Volume;
import com.profitbricks.rest.domain.raw.DataCenterRaw;
import com.profitbricks.rest.domain.raw.NicRaw;
import com.profitbricks.rest.domain.raw.ServerRaw;
import com.profitbricks.rest.domain.raw.VolumeRaw;
import com.profitbricks.sdk.ProfitbricksApi;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ali
 */
public class main {

   static ProfitbricksApi profitbricksApi = new ProfitbricksApi();
   static String createddataCenterId = "";
   static Server newServer = null;

   public static void main(String[] args) {
      setupCreds();

      ListDataCenters();
      CreateDC();
      createServer();
      updateCoresMemory();
      attachDetachVolume();
      listeverything();
      createNIC();
      deleteDC();

   }

   static void setupCreds() {
      profitbricksApi.setCredentials("xxxxx", "xxxxxxx");

   }

   private static void ListDataCenters() {
      try {
         List<DataCenter> datacenters = profitbricksApi.getDataCenterApi().getAllDataCenters();
      } catch (Exception ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private static void CreateDC() {
      try {
         DataCenterRaw datacenter = new DataCenterRaw();

         datacenter.getProperties().setName("SDK TEST DC - Data center");
         datacenter.getProperties().setLocation(Location.US_LAS.value());
         datacenter.getProperties().setDescription("SDK TEST Description");

         DataCenter newDatacenter = profitbricksApi.getDataCenterApi().createDataCenter(datacenter);
         createddataCenterId = newDatacenter.getId();
      } catch (Exception ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private static void deleteDC() {
      try {
         Thread.sleep(5000);
         profitbricksApi.getDataCenterApi().deleteDataCenter(createddataCenterId);
      } catch (Exception ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private static void createServer() {
      try {
         ServerRaw server = new ServerRaw();
         server.getProperties().setName("SDK TEST SERVER - Server");
         server.getProperties().setRam("1024");
         server.getProperties().setCores("4");
         server.getProperties().setCpuFamily("AMD_OPTERON");

         newServer = profitbricksApi.getServerApi().createServer(createddataCenterId, server);
      } catch (Exception ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private static void updateCoresMemory() {
      try {
         Thread.sleep(5000);
         PBObject object = new PBObject();
         object.setName("SDK TEST SERVER CHANGED");
         object.setRam("1024");
         object.setCores("4");

         Server updatedServer = profitbricksApi.getServerApi().updateServer(createddataCenterId, newServer.getId(), object);
      } catch (Exception ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private static void attachDetachVolume() {
      try {
         // First we need to create a volume.
         VolumeRaw volume = new VolumeRaw();
         volume.getProperties().setName("SDK TEST VOLUME - Volume");
         volume.getProperties().setSize("1024");
         volume.getProperties().setLicenceType("LINUX");
         volume.getProperties().setType("HDD");

         Volume newVolume = profitbricksApi.getVolumeApi().createVolume(createddataCenterId, volume);

         Thread.sleep(5000);
// Then we are going to attach the new volume to a server.
         Volume attachedVolume = profitbricksApi.getVolumeApi().attachVolume(createddataCenterId, newServer.getId(), newVolume.getId());

// Here we are going to detach it from the server.
         profitbricksApi.getVolumeApi().detachVolume(createddataCenterId, newServer.getId(), newVolume.getId());
      } catch (Exception ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private static void listeverything() {
      try {
         List<DataCenter> datacenters = profitbricksApi.getDataCenterApi().getAllDataCenters();

         List<Server> servers = profitbricksApi.getServerApi().getAllServers(createddataCenterId);

         List<Volume> volumes = profitbricksApi.getVolumeApi().getAllVolumes(createddataCenterId);
      } catch (Exception ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private static void createNIC() {
      try {
         Thread.sleep(5000);
         NicRaw nic = new NicRaw();

         nic.getProperties().setName("SDK TEST NIC - Nic");
         nic.getProperties().setLan("1");

         nic.getEntities().setFirewallrules(null);

         Nic newNic = profitbricksApi.getNicApi().createNic(createddataCenterId, newServer.getId(), nic);
      } catch (Exception ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
